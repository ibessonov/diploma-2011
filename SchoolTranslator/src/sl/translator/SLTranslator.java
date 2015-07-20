package sl.translator;

//<editor-fold defaultstate="collapsed" desc="Imports">
import sl.elements.StackElement;
import sl.elements.StackElementException;
import sl.elements.Type;
import sl.elements.integer.IntegerElement;
import sl.elements.integer.IntegerType;
import sl.elements.array.ArrayType;
import sl.elements.bool.BooleanType;
import sl.elements.record.RecordType;
import sl.operations.binary.BinaryOperation;
import sl.operations.unary.UnaryOperation;
import sl.operations.ExecuteOperation;
import sl.parser.nodes.Node;
import sl.parser.Token;
import sl.parser.TokenKind;
import sl.parser.TokenPosition;
import sl.parser.nodes.*;
import sl.program.Command;
import sl.program.DebugInformation;
import sl.program.Program;
import sl.program.commands.*;
import sl.plugins.CallExternalFunctionCommand;
import sl.plugins.Function;
import sl.plugins.Parameter;

import static sl.operations.binary.BinaryOperation.*;
import static sl.translator.SLParseTreeVisitorError.*;
//</editor-fold>

public final class SLTranslator extends ParseTreeVisitor {

    private TypesTable typesTable;
    private FunctionsTable functionsTable;
    private Program mainProgram;
    private FunctionBuilder currentFunction;
    private SLDebugInformation debugInfo;

    public SLTranslator() {
        clear();
    }

    public Program getProgram() {
        return mainProgram;
    }

    public DebugInformation getDebugInformation() {
        return debugInfo;
    }

    private void clear() {
        typesTable = new TypesTable();
        functionsTable = new FunctionsTable();
        mainProgram = new Program();
        currentFunction = null;
        debugInfo = new SLDebugInformation();
        currentRow = SLDebugInformation.SKIP_LINE;
    }

    @Override
    protected Void visit(NodeStart node, Object data)
            throws ParseTreeVisitorException {
        clear();
        try {
            node.getChild(1).accept(this, null); // Types

            CallCommand callCommand = new CallCommand(0);
            mainProgram.append(callCommand);
            debugInfo.addCommandRow(SLDebugInformation.SKIP_LINE);

            JumpCommand jumpCommand = new JumpCommand(0);
            mainProgram.append(jumpCommand);
            debugInfo.addCommandRow(SLDebugInformation.SKIP_LINE);

            node.getChild(2).accept(this, null); // функции

            callCommand.setAddress(mainProgram.size());

            addFunctionToFunctionsTable(node.getFirstToken().getNextToken(), null);
            node.getChild(3).accept(this, null); // переменные
            node.getChild(4).accept(this, null); // тело функции

            currentRow = SLDebugInformation.SKIP_LINE;
            addReturnToCurrentFunction();
            appendCurrentFunctionToMainProgram();

            jumpCommand.setIndex(mainProgram.size());
        } catch (ParseTreeVisitorException ex) {
            clear();
            throw ex;
        }
        return null;
    }

    @Override
    protected Void visit(NodeTypes node, Object data)
            throws ParseTreeVisitorException {
        int n = node.getNumChildren() / 2;
        for (int i = 0; i < n; ++i) {
            Token name = node.getChild(2 * i).getFirstToken();
            Type type = getTypeAccept(node.getChild(2 * i + 1), name.getImage());
            typesTable.add(name, type);
        }
        return null;
    }

    @Override
    protected Type visit(NodeTypeDeclaration node, Object data)
            throws ParseTreeVisitorException {
        String typeName = data.toString();
        Token token = node.getFirstToken();
        if (token.getKind() == TokenKind.RECORD) {
            RecordType record = new RecordType(typeName);
            int n = node.getNumChildren() / 2;
            for (int i = 0; i < n; ++i) {
                Type type = getTypeAccept(node.getChild(2 * i), null);
                Token identifier = node.getChild(2 * i + 1).getFirstToken();
                String name = identifier.getImage();
                if (record.isIn(name)) {
                    throw new SLParseTreeVisitorException(
                            identifier.getPosition(), RECORD_FIELD_ALREADY_EXISTS,
                            data.toString(), name);
                }
                record.addField(name, type);
            }
            return record;
        }
        Type type = getTypeAccept(node.getChild(0), null);
        if (node.getNumChildren() == 1) {
            return type;
        }
        return getArrayType(node, 1, node.getNumChildren() / 2, type, typeName);
    }

    @Override
    protected Integer visit(NodeIntegerConstant node, Object data) {
        Token token = node.getFirstToken();
        if (token.getKind() == TokenKind.MINUS) {
            return -(Integer.valueOf(token.getNextToken().getImage()));
        } else {
            return Integer.valueOf(token.getImage());
        }
    }

    @Override
    protected Type visit(NodeType node, Object data)
            throws ParseTreeVisitorException {
        return typesTable.get(node.getFirstToken());
    }

    @Override
    protected Void visit(NodeFunctions node, Object data)
            throws ParseTreeVisitorException {
        int i = 0;
        while (i < node.getNumChildren()) {
            Type type = (node.getChild(i) instanceof NodeType)
                    ? getTypeAccept(node.getChild(i++), null)
                    : null;
            Token name = node.getChild(i++).getFirstToken();
            setCurrentRow(name.getPosition().getBeginLine());
            try {
                addFunctionToFunctionsTable(name, type);
            } catch (ParseTreeVisitorException ex) {
                throw new SLParseTreeVisitorException(name.getPosition(),
                        FUNCTION_ALREADY_EXISTS, name.getImage());
            }
            while (node.getChild(i) instanceof NodeFormalParameter) {
                node.getChild(i++).accept(this, null);
            }
            node.getChild(i++).accept(this, null); //variables
            node.getChild(i++).accept(this, null); //block
            if (type == null) {
                addReturnToCurrentFunction();
            } else {
                addCommandToCurrentFunction(new RuntimeErrorCommand(
                        new RuntimeException(String.format(
                        "Неверный выход из функции %s: "
                        + "отсутствует возвращаемое значение", name.getImage())
                        )));
            }
            appendCurrentFunctionToMainProgram();
        }
        return null;
    }

    @Override
    protected Void visit(NodeFormalParameter node, Object data)
            throws ParseTreeVisitorException {
        Type type = getTypeAccept(node.getChild(0), null);
        Token name = node.getChild(1).getFirstToken();
        Token paramType = node.getFirstToken();
        addParameterToCurrentFunction(name, type,
                paramType.getKind() == TokenKind.RESULT);
        return null;
    }

    @Override
    protected Void visit(NodeVariables node, Object data)
            throws ParseTreeVisitorException {
        int n = node.getNumChildren();
        Type currentType = null;
        for (int i = 0; i < n; ++i) {
            Node current = node.getChild(i);
            if (current instanceof NodeType) {
                currentType = getTypeAccept(current, null);
                continue;
            }
            current.accept(this, currentType);
        }
        return null;
    }

    @Override
    protected String visit(NodeIdentifier node, Object data) {
        return node.getFirstToken().getImage();
    }

    @Override
    protected Void visit(NodeVariableDeclaration node, Object data)
            throws ParseTreeVisitorException {
        Type type = (Type) data;
        Token name = node.getChild(0).getFirstToken();
        if (node.getNumChildren() == 1) {
            addVariableToCurrentFunction(name, type);
        } else {
            addVariableToCurrentFunction(name, getArrayType(node, 1,
                    node.getNumChildren() / 2, type, null));
        }
        return null;
    }

    private Type getArrayType(Node node, int start, int count, Type type, String typeName)
            throws ParseTreeVisitorException {
        ArrayType arrayType = new ArrayType(type, typeName);
        for (int i = 0; i < count; ++i) {
            int from = (Integer) node.getChild(start++).accept(this, null);
            int to = (Integer) node.getChild(start++).accept(this, null);
            if (from > to) {
                throw new SLParseTreeVisitorException(node.getChild(start - 1).
                        getFirstToken().getPosition(), LOWBOUND_IN_ARRAY_MORE_HIGHBOUND);
            }
            arrayType.addDimention(from, to);
        }
        return arrayType;
    }

    @Override
    protected Object visit(NodeBlock node, Object data)
            throws ParseTreeVisitorException {
        int n = node.getNumChildren();
        for (int i = 0; i < n; ++i) {
            node.getChild(i).accept(this, null);
        }
        return null;
    }

    @Override
    protected Void visit(NodeStatement node, Object data)
            throws ParseTreeVisitorException {
        setCurrentRow(node.getFirstToken().getPosition().getBeginLine());
        Node child = node.getChild(0);
        if (getTypeAccept(child, null) != null) {
            addCommandToCurrentFunction(new ClearTopCommand());
        }
        return null;
    }

    @Override
    protected Void visit(NodeSwitchStatement node, Object data)
            throws ParseTreeVisitorException {
        pushIntegerExpression(node.getChild(0), EXPRESSION_IN_SWITCH_NOT_INTEGER);
        JumpCommand jump = new JumpCommand(0);
        BinaryOperationCommand compare = new BinaryOperationCommand(COMPARISON_EQUALS);
        int n = (node.getNumChildren() - 1) / 2;
        for (int i = 0; i < n; ++i) {
            Node caseNode = node.getChild(2 * i + 1);
            setCurrentRow(caseNode.getFirstToken().getPosition().getBeginLine());
            addCommandToCurrentFunction(new PushTopCommand());
            addConstantToCurrentFunction(new IntegerElement(
                    (Integer) caseNode.accept(this, null)));
            addCommandToCurrentFunction(compare);
            JumpIfFalseCommand jumpIfFalse = new JumpIfFalseCommand(0);
            addCommandToCurrentFunction(jumpIfFalse);
            node.getChild(2 * i + 2).accept(this, data);
            addCommandToCurrentFunction(new ClearTopCommand());
            addCommandToCurrentFunction(jump);
            jumpIfFalse.setIndex(getCurrentFunction().getCommandsCount());
        }
        if ((n = node.getNumChildren()) % 2 == 0) {
            setCurrentRow(node.getChild(n - 1).getFirstToken().getPosition().getBeginLine());
            node.getChild(n - 1).accept(this, data);
            addCommandToCurrentFunction(new ClearTopCommand());
        }
        jump.setIndex(getCurrentFunction().getCommandsCount());
        return null;
    }

    @Override
    protected Object visit(NodeCycleStatement node, Object data)
            throws ParseTreeVisitorException {
        return node.getChild(0).accept(this, null);
    }

    @Override
    protected Void visit(NodeForStatement node, Object data)
            throws ParseTreeVisitorException {
        try {
            pushIntegerExpression(node.getChild(0), VARIABLE_IN_FOR_NOT_INTEGER);
        } catch (ParseTreeVisitorException ex) {
            throw new SLParseTreeVisitorException(node.getFirstToken().getPosition(),
                    VARIABLE_IN_FOR_NOT_INTEGER);
        }
        int currentRowInFor = currentRow;
        //инициализация
        addCommandToCurrentFunction(new PushTopCommand());
        pushIntegerExpression(node.getChild(1), EXPRESSION_FROM_IN_FOR_NOT_INTEGER);
        addCommandToCurrentFunction(new AssignCommand());
        //условие выхода
        int address = getCurrentFunction().getCommandsCount();
        addCommandToCurrentFunction(new PushTopCommand());
        pushIntegerExpression(node.getChild(2), EXPRESSION_TO_IN_FOR_NOT_INTEGER);
        addBinaryOperationToCurrentFunction(COMPARISON_GREATER);
        JumpIfTrueCommand jumpToEnd = new JumpIfTrueCommand(0);
        addCommandToCurrentFunction(jumpToEnd);
        //тело цикла
        node.getChild(3).accept(this, data);
        currentRow = currentRowInFor;
        addCommandToCurrentFunction(new IncrementCommand());
        addCommandToCurrentFunction(new JumpCommand(address));
        //конец цикла
        jumpToEnd.setIndex(getCurrentFunction().getCommandsCount());
        addCommandToCurrentFunction(new ClearTopCommand());
        return null;
    }

    @Override
    protected Void visit(NodeWhileStatement node, Object data)
            throws ParseTreeVisitorException {
        setCurrentRow(node.getFirstToken().getNextToken().getPosition().getBeginLine());
        int begin = getCurrentFunction().getCommandsCount();
        pushCondition(node.getChild(0));
        JumpIfFalseCommand jump = new JumpIfFalseCommand(0);
        addCommandToCurrentFunction(jump);
        node.getChild(1).accept(this, data);
        addCommandToCurrentFunction(new JumpCommand(begin));
        jump.setIndex(getCurrentFunction().getCommandsCount());
        return null;
    }

    @Override
    protected Void visit(NodeRepeatStatement node, Object data)
            throws ParseTreeVisitorException {
        int begin = getCurrentFunction().getCommandsCount();
        node.getChild(0).accept(this, data);
        setCurrentRow(node.getChild(1).getFirstToken().getPosition().getBeginLine());
        pushCondition(node.getChild(1));
        addCommandToCurrentFunction(new JumpIfTrueCommand(begin));
        return null;
    }

    private void pushCondition(Node node) throws ParseTreeVisitorException {
        Expression condition = getExpressionAccept(node, null);
        try {
            BooleanType.get().convert(condition.type());
        } catch (StackElementException ex) {
            throw new SLParseTreeVisitorException(
                    node.getFirstToken().getPosition(),
                    EXPRESSION_NOT_BOOLEAN);
        }
        pushExpression(condition);
    }

    private void pushIntegerExpression(Node node, SLParseTreeVisitorError error)
            throws ParseTreeVisitorException {
        Expression expression = getExpressionAccept(node, null);
        try {
            IntegerType.get().convert(expression.type());
        } catch(StackElementException ex) {
            throw new SLParseTreeVisitorException(
                    node.getFirstToken().getPosition(), error);
        }
        pushExpression(expression);
    }

    @Override
    protected Void visit(NodeReturnStatement node, Object data)
            throws ParseTreeVisitorException {
        Type expected = getCurrentFunction().getReturnType();
        if (node.getNumChildren() == 0) {
            if (expected != null) {
                throw new SLParseTreeVisitorException(node.getFirstToken().getPosition(),
                        RETURN_STATEMENT_IN_FUNCTION_IS_ABSENT);
            }
        } else {
            if (expected == null) {
                throw new SLParseTreeVisitorException(node.getFirstToken().getPosition(),
                        RETURN_STATEMENT_IN_FUNCTION_IS_AXCESS);
            }
            Expression result = getExpressionAccept(node.getChild(0), null);
            try {
                expected.convert(result.type());
            } catch (StackElementException ex) {
                throw new SLParseTreeVisitorException(
                        node.getChild(0).getFirstToken().getPosition(),
                        WRONG_RETURN_TYPE, expected, result.type());
            }
            pushExpression(result);
        }
        addReturnToCurrentFunction();
        return null;
    }

    @Override
    protected Void visit(NodeIfStatement node, Object data)
            throws ParseTreeVisitorException {
        pushCondition(node.getChild(0));
        JumpIfFalseCommand jumpIfFalse = new JumpIfFalseCommand(0);
        addCommandToCurrentFunction(jumpIfFalse);
        node.getChild(1).accept(this, data);
        if (node.getNumChildren() > 2) {
            JumpCommand jump = new JumpCommand(0);
            addCommandToCurrentFunction(jump);
            jumpIfFalse.setIndex(getCurrentFunction().getCommandsCount());
            node.getChild(2).accept(this, data);
            jump.setIndex(getCurrentFunction().getCommandsCount());
        } else {
            jumpIfFalse.setIndex(getCurrentFunction().getCommandsCount());
        }
        return null;
    }

    @Override
    protected Type visit(NodeCallFunction node, Object data)
            throws ParseTreeVisitorException {
        Token name = node.getChild(0).getFirstToken();
        Function function = functionsTable.getFunction(name);
        int entrance = functionsTable.getEntrance(name);
        int n = node.getNumChildren();
        if (function.getParamsCount() != n - 1) {
            throw new SLParseTreeVisitorException(node.getFirstToken().
                    getPosition(), NUMBER_OF_PARAMETERS_WRONG,
                    name.getImage(), n - 1, function.getParamsCount());
        }
        for (int i = 1; i < n; ++i) {
            Parameter expected = function.getParameter(i - 1);
            Expression parameter = getExpressionAccept(node.getChild(i), null);
            try {
                expected.type().convert(parameter.type());
            } catch (StackElementException ex) {
                throw new SLParseTreeVisitorException(
                        node.getChild(i).getFirstToken().getPosition(),
                        WRONG_TYPE_OF_PARAMETER,
                        expected.type(), parameter.type());
            }
            TokenPosition position = node.getChild(i).getFirstToken().getPosition();
            if (expected.isResult()) {
                if (parameter.isConstant()) {
                    throw new SLParseTreeVisitorException(position, MUSTNOT_SENT_CONSTANT_BY_LINK);
                }
                if (!parameter.isLeftValue()) {
                    throw new SLParseTreeVisitorException(position, WRONG_PARAMETER_BY_LINK);
                }
                if (!expected.type().equals(parameter.type())) {
                    throw new SLParseTreeVisitorException(position, WRONG_TYPE_OF_PARAMETER, expected.type().toString(),
                            parameter.type().toString());
                }
            } else {
                pushExpression(parameter);
                if (!parameter.type().equals(expected.type())) {
                    addCommandToCurrentFunction(new ConvertCommand(expected.type()));
                }
                if(parameter.isLeftValue()) {
                    addCommandToCurrentFunction(new ReplaceTopToCloneCommand());
                }
            }
        }
        addCommandToCurrentFunction(
                entrance == FunctionsTable.EXTERNAL_FUNCTION
                ? new CallExternalFunctionCommand(name.getImage())
                : new CallCommand(entrance));
        return function.getReturnType();
    }

    @Override
    protected Expression visit(NodeLeftValue node, Object data)
            throws ParseTreeVisitorException {
        return visitLeftValue(node);
    }

    @Override
    protected Boolean visit(NodeBracketsInExpression node, Object data)
            throws ParseTreeVisitorException {
        if (node.getNumChildren() == 0) {
            return false;
        }
        TokenPosition position = node.getFirstToken().getPosition();
        if (!(data instanceof ArrayType)) {
            throw new SLParseTreeVisitorException(position,
                    EXPRESSION_MUST_BE_ARRAY_TYPE, data.toString());
        }
        ArrayType type = (ArrayType) data;
        int n = node.getNumChildren();
        if (n != type.dimentions()) {
            throw new SLParseTreeVisitorException(position, DIMENTIONS_OF_ARRAY_IS_WRONG,
                    type.dimentions(), n);
        }
        pushIntegerExpression(node.getChild(0), EXPRESSION_NOT_INTEGER);
        for (int i = 1; i < n; ++i) {
            addConstantToCurrentFunction(new IntegerElement(type.count(i)));
            addBinaryOperationToCurrentFunction(MULTIPLICATION);
            pushIntegerExpression(node.getChild(i), EXPRESSION_NOT_INTEGER);
            addBinaryOperationToCurrentFunction(ADDITION);
        }
        addCommandToCurrentFunction(new ArrayGetCommand());
        return true;
    }

    @Override
    protected Void visit(NodeAssignment node, Object data)
            throws ParseTreeVisitorException {
        Expression left = getExpressionAccept(node.getChild(0), null);
        Expression right = getExpressionAccept(node.getChild(1), null);
        try {
            left.type().convert(right.type());
        } catch (StackElementException ex) {
            throw new SLParseTreeVisitorException(node.getChild(1).getFirstToken().
                    getPosition(), MUSTNOT_DO_CONVERTABLE_TYPES_FOR_ASSIGNABLE,
                    left.type(), right.type());
        }
        pushExpression(right);
        addCommandToCurrentFunction(new AssignCommand());
        return null;
    }

    private Expression visitBinaryOperation(BinaryOperation operation,
            Expression first, Expression next)
            throws StackElementException {
        if (next.isConstant()) {
            if (first.isConstant()) {
                first = new Expression(ExecuteOperation.binary(
                        operation, first.element(), next.element()));
            } else {
                addConstantToCurrentFunction(next.element());
                addBinaryOperationToCurrentFunction(operation);
                first = new Expression(resultType(operation,
                        first.type(), next.type()), false);
            }
        } else { // !next.isConatant()
            if (first.isConstant()) {
                addConstantToCurrentFunction(first.element());
                addCommandToCurrentFunction(new SwapCommand());
            }
            addBinaryOperationToCurrentFunction(operation);
            first = new Expression(resultType(operation,
                    first.type(), next.type()), false);
        }
        return first;
    }

    private Expression visitBinaryOperation(BinaryOperation operation,
            Node node)
            throws ParseTreeVisitorException {
        int n = node.getNumChildren();
        Expression first = getExpressionAccept(node.getChild(0), null);
        if (n == 1) {
            return first;
        }
        for (int i = 1; i < n; ++i) {
            Expression next = null;
            try {
                next = getExpressionAccept(node.getChild(i), null);
                first = visitBinaryOperation(operation, first, next);
            } catch (StackElementException e) {
                throw new SLParseTreeVisitorException(
                        node.getChild(i).getFirstToken().getPosition(),
                        BINARY_OPERATION_NOT_SUPPORTED,
                        operation, first.type(), next.type());
            } catch (RuntimeException e) {
                throw new ParseTreeVisitorException(e.toString(), null);
            }
        }
        return first;
    }

    @Override
    protected Expression visit(NodeExpression node, Object data)
            throws ParseTreeVisitorException {
        return visitBinaryOperation(DISJUNCTION, node);
    }

    @Override
    protected Expression visit(NodeConditionalAndExpression node, Object data)
            throws ParseTreeVisitorException {
        return visitBinaryOperation(CONJUNCTION, node);
    }

    @Override
    protected Expression visit(NodeRelationExpression node, Object data)
            throws ParseTreeVisitorException {
        return visitBinaryOperation(node);
    }

    @Override
    protected BinaryOperation visit(NodeRelationSign node, Object data) {
        switch (node.getFirstToken().getKind()) {
            case EQUALS:
                return BinaryOperation.COMPARISON_EQUALS;
            case NOT_EQUALS:
                return BinaryOperation.COMPARISON_NOT_EQUALS;
            case GREATER:
                return BinaryOperation.COMPARISON_GREATER;
            case LESS:
                return BinaryOperation.COMPARISON_LESS;
            case LESS_OR_EQUALS:
                return BinaryOperation.COMPARISON_NOT_GREATER;
            case GREATER_OR_EQUALS:
                return BinaryOperation.COMPARISON_NOT_LESS;
            default:
                return null;
        }
    }

    @Override
    protected Expression visit(NodeAdditiveExpression node, Object data)
            throws ParseTreeVisitorException {
        return visitBinaryOperation(node);
    }

    @Override
    protected BinaryOperation visit(NodeAdditiveSign node, Object data) {
        switch (node.getFirstToken().getKind()) {
            case PLUS:
                return BinaryOperation.ADDITION;
            case MINUS:
                return BinaryOperation.SUBTRACTION;
            default:
                return null;
        }
    }

    @Override
    protected Expression visit(NodeMultiplicativeExpression node, Object data)
            throws ParseTreeVisitorException {
        return visitBinaryOperation(node);
    }

    private Expression visitBinaryOperation(SimpleNode node)
            throws ParseTreeVisitorException {
        int n = node.getNumChildren();
        Expression first = getExpressionAccept(node.getChild(0), null);
        if (n == 1) {
            return first;
        }
        for (int i = 0; i < n / 2; ++i) {
            Node sign = node.getChild(2 * i + 1);
            BinaryOperation operation = (BinaryOperation) sign.accept(this, null);
            Expression next = getExpressionAccept(node.getChild(2 * i + 2), null);
            try {
                first = visitBinaryOperation(operation, first, next);
            } catch (StackElementException e) {
                throw new SLParseTreeVisitorException(
                        sign.getFirstToken().getPosition(),
                        BINARY_OPERATION_NOT_SUPPORTED,
                        operation, first.type(), next.type());
            } catch (RuntimeException e) {
                throw new ParseTreeVisitorException(e.toString(), null);
            }
        }
        return first;
    }

    @Override
    protected BinaryOperation visit(NodeMultiplicativeSign node, Object data) { // +
        switch (node.getFirstToken().getKind()) {
            case MULTIPLY:
                return BinaryOperation.MULTIPLICATION;
            case DIVIDE:
                return BinaryOperation.DIVISION;
            default:
                return null;
        }
    }

    @Override
    protected Expression visit(NodeUnaryExpression node, Object data)
            throws ParseTreeVisitorException {
        Node sign = node.getChild(0);
        UnaryOperation operation = (UnaryOperation) sign.accept(this, null);
        Expression value = getExpressionAccept(node.getChild(1), null);
        if (operation == null) {
            return value;
        }
        try {
            if (value.isConstant()) {
                return new Expression(ExecuteOperation.unary(operation,
                        value.element()));
            }
            Type resType = value.type().operationResult(operation);
            addUnaryOperationToCurrentFunction(operation);
            return new Expression(resType, false);
        } catch (StackElementException ex) {
            throw new SLParseTreeVisitorException(sign.getFirstToken().getPosition(),
                    UNARY_OPERATION_NOT_SUPPORTED,
                    operation.toString(), value.type().toString());
        }
    }

    @Override
    protected UnaryOperation visit(NodeUnarySign node, Object data) {
        switch (node.getFirstToken().getKind()) {
            case MINUS:
                return UnaryOperation.MINUS;
            case NOT:
                return UnaryOperation.NEGATION;
            default:
                return null;
        }
    }

    @Override
    protected Expression visit(NodeUnaryValue node, Object data)
            throws ParseTreeVisitorException {
        Token token = node.getFirstToken();
        StackElement elem = ConstantsParser.parse(token);
        if (elem != null) {
            return new Expression(elem);
        }
        Node child = node.getChild(0);
        if (child instanceof NodeExpression) {
            return getExpressionAccept(child, null);
        }
        if (child instanceof NodeCallFunction) {
            Type result = getTypeAccept(child, null);
            if (result != null) {
                return new Expression(result, false);
            }
            throw new SLParseTreeVisitorException(token.getPosition(),
                    RETURN_STATEMENT_IN_FUNCTION_IS_ABSENT);
        }
        return visitLeftValue(node);
    }

    private Expression visitLeftValue(Node node)
            throws ParseTreeVisitorException {
        Token name = node.getChild(0).getFirstToken();
        Variable var = getCurrentFunction().getVariable(name);
        Type currentType = var.type();
        addCommandToCurrentFunction(new PushCommand(var.getOffset()));
        int n = node.getNumChildren();
        for (int i = 1; i < n; ++i) {
            while(i < n && node.getChild(i) instanceof NodeBracketsInExpression) {
                if (getBooleanAccept(node.getChild(i), currentType)) {
                    currentType = ((ArrayType) currentType).type();
                }
                i++;
            }
            if (i == n) {
                break;
            }
            Token token = node.getChild(i).getFirstToken();
            String field = token.getImage();
            if (!(currentType instanceof RecordType)) {
                throw new SLParseTreeVisitorException(token.getPosition(),
                        EXPRESSION_MUST_BE_RECORD_TYPE, currentType);
            }
            if (!((RecordType) currentType).isIn(field)) {
                throw new SLParseTreeVisitorException(token.getPosition(),
                        RECORD_FIELD_NOT_FOUND, currentType, field);
            }
            addCommandToCurrentFunction(new RecordGetCommand(field));
            currentType = ((RecordType) currentType).getFieldType(field);
        }
        return new Expression(currentType, true);
    }

    @Override
    protected Void visit(NodeReadStatement node, Object data)
            throws ParseTreeVisitorException {
        for (int i = 0; i < node.getNumChildren(); ++i) {
            setCurrentRow(node.getChild(i).getFirstToken().getPosition().getBeginLine());
            node.getChild(i).accept(this, data);
            addCommandToCurrentFunction(new ReadCommand());
        }
        return null;
    }

    @Override
    protected Void visit(NodeWriteStatement node, Object data)
            throws ParseTreeVisitorException {
        for (int i = 0; i < node.getNumChildren(); ++i) {
            setCurrentRow(node.getChild(i).getFirstToken().getPosition().getBeginLine());
            Expression expression = getExpressionAccept(node.getChild(i), data);
            pushExpression(expression);
            addCommandToCurrentFunction(new WriteCommand());
        }
        return null;
    }

//##############################################################################
    
    private Type getTypeAccept(Node node, Object data)
            throws ParseTreeVisitorException {
        return (Type) node.accept(this, data);
    }

    private Expression getExpressionAccept(Node node, Object data)
            throws ParseTreeVisitorException {
        return (Expression) node.accept(this, data);
    }

    private boolean getBooleanAccept(Node node, Object data)
            throws ParseTreeVisitorException {
        return (Boolean) node.accept(this, data);
    }

    private void pushExpression(Expression expression) {
        if (expression.isConstant()) {
            addConstantToCurrentFunction(expression.element());
        }
    }

    private static Type resultType(BinaryOperation id, Type left, Type right)
            throws StackElementException {
        try {
            return left.convert(right).operationResult(id);
        } catch (StackElementException e) {
            return right.convert(left).operationResult(id);
        }
    }

    private FunctionBuilder getCurrentFunction() {
        return currentFunction;
    }

    private void addCommandToCurrentFunction(Command command) {
        getCurrentFunction().addCommand(command);
        debugInfo.addCommandRow(getCurrentRow());
    }

    private void addFunctionToFunctionsTable(Token name, Type type)
            throws ParseTreeVisitorException {
        currentFunction = new FunctionBuilder(name.getImage(), type);
        functionsTable.add(name, currentFunction, mainProgram.size());
        debugInfo.addFunction(name.getImage(), mainProgram.size());
    }

    private void appendCurrentFunctionToMainProgram() {
        mainProgram.append(currentFunction.getProgram());
    }

    private void addParameterToCurrentFunction(Token token, Type type,
            boolean result) throws ParseTreeVisitorException {
        String name = token.getImage();
        Variable variable = currentFunction.addParameter(token, type, result);
        debugInfo.addVariable(currentFunction.getName(), name, variable);
    }

    private void addVariableToCurrentFunction(Token token, Type type)
            throws ParseTreeVisitorException {
        Variable variable = currentFunction.addVariable(token, type);
        addCommandToCurrentFunction(new InitializeCommand(type));
        debugInfo.addVariable(currentFunction.getName(), token.getImage(), variable);
    }

    private void addReturnToCurrentFunction() {
        addCommandToCurrentFunction(new ReturnCommand(currentFunction.getParamsCount(),
                currentFunction.getReturnType() != null));
    }

    private void addUnaryOperationToCurrentFunction(UnaryOperation id) {
        addCommandToCurrentFunction(new UnaryOperationCommand(id));
    }

    private void addBinaryOperationToCurrentFunction(BinaryOperation id) {
        addCommandToCurrentFunction(new BinaryOperationCommand(id));
    }

    private void addConstantToCurrentFunction(StackElement elem) {
        addCommandToCurrentFunction(new PushConstantCommand(elem));
    }

    /*
     * Следим за позицией текущей строки
     */
    private int currentRow = 0;

    private void setCurrentRow(int row) {
        this.currentRow = row - 1; //-1 НУЖНА, НЕ СТИРАЙ
    }

    private int getCurrentRow() {
        return this.currentRow;
    }
}
