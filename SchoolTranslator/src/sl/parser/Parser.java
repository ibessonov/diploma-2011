package sl.parser;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import sl.parser.nodes.*;
import static sl.parser.TokenKind.*;

public final class Parser {

    private StreamTokenizer tokenizer;
    private Token current;
    private ParseTreeBuilder builder;

    public Parser(InputStream input) {
        tokenizer = new StreamTokenizer(input);
        current = null;
    }

    public Parser(InputStream input, String encoding)
            throws UnsupportedEncodingException {
        tokenizer = new StreamTokenizer(input, encoding);
        current = null;
    }

    public void reInit(InputStream input) {
        tokenizer.reInit(input);
        current = null;
    }

    public void reInit(InputStream input, String encoding)
            throws UnsupportedEncodingException {
        tokenizer.reInit(input, encoding);
        current = null;
    }

    public void parse(ParseTreeBuilder builder) throws ParseException {
        this.builder = builder;
        nodeStart();
    }

    private Token consumeToken(TokenKind kind) throws ParseException {
        Token token = getNextToken();
        if (token.getKind() != kind) {
            throw new ParseException(token.getKind(), token.getPosition());
        }
        return token;
    }

    //нельзя вызывать если current==null, потеряем первый токен
    private Token lookup() throws ParseException {
        Token temp = current;
        try {
            return getNextToken();
        } finally {
            current = temp;
        }
    }

    private boolean checkAndConsumeToken(TokenKind kind) throws ParseException {
        Token temp = current;
        getNextToken();
        if (current.getKind() == kind) {
            return true;
        } else {
            current = temp;
            return false;
        }
    }

    private boolean checkToken(TokenKind kind) throws ParseException {
        return lookup().getKind() == kind;
    }

    private boolean checkTypeToken() throws ParseException {
        switch (lookup().getKind()) {
            case INTEGER:
            case REAL:
            case CHAR:
            case STRING:
            case BOOLEAN:
                return true;
            default:
                return false;
        }
    }

    private boolean checkRelationSignToken() throws ParseException {
        switch (lookup().getKind()) {
            case GREATER:
            case GREATER_OR_EQUALS:
            case LESS:
            case LESS_OR_EQUALS:
            case EQUALS:
            case NOT_EQUALS:
                return true;
            default:
                return false;
        }
    }

    private boolean checkAdditiveSignToken() throws ParseException {
        switch (lookup().getKind()) {
            case PLUS:
            case MINUS:
                return true;
            default:
                return false;
        }
    }

    private boolean checkMultiplicativeSignToken() throws ParseException {
        switch (lookup().getKind()) {
            case MULTIPLY:
            case DIVIDE:
                return true;
            default:
                return false;
        }
    }

    private boolean checkUnarySignToken() throws ParseException {
        switch (lookup().getKind()) {
            case MINUS:
            case NOT:
                return true;
            default:
                return false;
        }
    }

    private Token getNextToken() throws ParseException {
        if (current != null) {
            if (current.getNextToken() == null) {
                current.setNextToken(tokenizer.nextToken());
            }
            return (current = current.getNextToken());
        }
        return (current = tokenizer.nextToken());
    }

    private void nodeStart() throws ParseException {
        consumeToken(ALGORITHM);
        Node node = new NodeStart(current);
        builder.openNode(node);
        nodeIdentifier();
        consumeToken(SEMICOLON);
        nodeTypes();
        nodeFunctions();
        nodeVariables();
        consumeToken(BEGIN);
        nodeBlock();
        consumeToken(END);
        consumeToken(EOF);
        builder.closeNode();
    }

    private void nodeIdentifier() throws ParseException {
        Node node = new NodeIdentifier(lookup());
        consumeToken(IDENTIFIER);
        builder.openNode(node);
        builder.closeNode();
    }

    private void nodeTypes() throws ParseException {
        Node node = new NodeTypes(lookup());
        builder.openNode(node);
        while (checkAndConsumeToken(TYPE)) {
            nodeIdentifier();
            consumeToken(EQUALS);
            nodeTypeDeclaration();
            consumeToken(SEMICOLON);
        }
        builder.closeNode();
    }

    private void nodeTypeDeclaration() throws ParseException {
        Node node = new NodeTypeDeclaration(lookup());
        builder.openNode(node);
        if (checkAndConsumeToken(RECORD)) {
            consumeToken(BEGIN);
            do {
                nodeType();
                nodeIdentifier();
                consumeToken(SEMICOLON);
            } while (!checkAndConsumeToken(END));
        } else {
            nodeType();
            if (checkAndConsumeToken(TABLE)) {
                consumeToken(OBRACKET);
                do {
                    nodeIntegerConstant();
                    consumeToken(COLON);
                    nodeIntegerConstant();
                } while (checkAndConsumeToken(COMMA));
                consumeToken(CBRACKET);
            }
        }
        builder.closeNode();
    }

    private void nodeIntegerConstant() throws ParseException {
        Node node = new NodeIntegerConstant(lookup());
        builder.openNode(node);
        checkAndConsumeToken(MINUS);
        consumeToken(INTEGER_LITERAL);
        builder.closeNode();
    }

    private void nodeType() throws ParseException {
        Node node = new NodeType(lookup());
        builder.openNode(node);
        if (checkTypeToken() || checkToken(IDENTIFIER)) {
            getNextToken();
        } else {
            throw new ParseException(lookup().getKind(), lookup().getPosition());
        }
        builder.closeNode();
    }

    private void nodeFunctions() throws ParseException {
        Node node = new NodeFunctions(lookup());
        builder.openNode(node);
        while (checkAndConsumeToken(ALGORITHM)) {
            Token temp = current;
            boolean type = checkTypeToken();
            if (!type) {
                consumeToken(IDENTIFIER);
                type = checkToken(IDENTIFIER);
            }
            current = temp;
            if (type) {
                nodeType();
            }
            nodeIdentifier();
            consumeToken(OPARENTHESIS);
            if (!checkAndConsumeToken(CPARENTHESIS)) {
                do {
                    nodeFormalParameter();
                } while (checkAndConsumeToken(COMMA));
                consumeToken(CPARENTHESIS);
            }
            nodeVariables();
            consumeToken(BEGIN);
            nodeBlock();
            consumeToken(END);
        }
        builder.closeNode();
    }

    private void nodeFormalParameter() throws ParseException {
        Node node = new NodeFormalParameter(lookup());
        builder.openNode(node);
        if (checkAndConsumeToken(ARGUMENT)
                || checkAndConsumeToken(RESULT)) {
            nodeType();
            nodeIdentifier();
        } else {
            throw new ParseException(lookup().getKind(), lookup().getPosition());
        }
        builder.closeNode();
    }

    private void nodeVariables() throws ParseException {
        Node node = new NodeVariables(lookup());
        builder.openNode(node);
        while (!checkToken(BEGIN)) {
            nodeType();
            do {
                nodeVariableDeclaration();
            } while (checkAndConsumeToken(COMMA));
            consumeToken(SEMICOLON);
        }
        builder.closeNode();
    }

    private void nodeVariableDeclaration() throws ParseException {
        Node node = new NodeVariableDeclaration(lookup());
        builder.openNode(node);
        boolean table = checkAndConsumeToken(TABLE);
        nodeIdentifier();
        if (table) {
            consumeToken(OBRACKET);
            do {
                nodeIntegerConstant();
                consumeToken(COLON);
                nodeIntegerConstant();
            } while (checkAndConsumeToken(COMMA));
            consumeToken(CBRACKET);
        }
        builder.closeNode();
    }

    private void nodeBlock() throws ParseException {
        Node node = new NodeBlock(lookup());
        builder.openNode(node);
        do {
            nodeStatement();
            consumeToken(SEMICOLON);
        } while (!checkToken(END)
                && !checkToken(CASE)
                && !checkToken(ELSE)
                && !checkToken(ALL)
                && !checkToken(ENDCYCLE)
                && !checkToken(WHILE));
        builder.closeNode();
    }

    private void nodeStatement() throws ParseException {
        Node node = new NodeStatement(lookup());
        builder.openNode(node);
        switch (lookup().getKind()) {
            case IF:
                nodeIfStatement();
                break;
            case SWITCH:
                nodeSwitchStatement();
                break;
            case BEGINCYCLE:
                nodeCycleStatement();
                break;
            case READ:
                nodeReadStatement();
                break;
            case WRITE:
                nodeWriteStatement();
                break;
            case RETURN:
                nodeReturnStatement();
                break;
            case IDENTIFIER:
                Token temp = current;
                getNextToken();
                boolean function = checkToken(OPARENTHESIS);
                current = temp;
                if (function) {
                    nodeCallFunction();
                } else {
                    nodeAssignment();
                }
                break;
            default:
                throw new ParseException(lookup().getKind(),
                        lookup().getPosition());
        }
        builder.closeNode();
    }

    private void nodeReadStatement() throws ParseException {
        Node node = new NodeReadStatement(lookup());
        builder.openNode(node);
        consumeToken(READ);
        do {
            nodeLeftValue();
        } while (checkAndConsumeToken(COMMA));
        builder.closeNode();
    }

    private void nodeWriteStatement() throws ParseException {
        Node node = new NodeWriteStatement(lookup());
        builder.openNode(node);
        consumeToken(WRITE);
        do {
            nodeExpression();
        } while (checkAndConsumeToken(COMMA));
        builder.closeNode();
    }

    private void nodeSwitchStatement() throws ParseException {
        Node node = new NodeSwitchStatement(lookup());
        builder.openNode(node);
        consumeToken(SWITCH);
        nodeExpression();
        while (checkAndConsumeToken(CASE)) {
            nodeIntegerConstant();
            consumeToken(COLON);
            nodeBlock();
        }
        if (checkAndConsumeToken(ELSE)) {
            consumeToken(COLON);
            nodeBlock();
        }
        consumeToken(ALL);
        builder.closeNode();
    }

    private void nodeCycleStatement() throws ParseException {
        Node node = new NodeCycleStatement(lookup());
        builder.openNode(node);
        consumeToken(BEGINCYCLE);
        switch (lookup().getKind()) {
            case FOR:
                nodeForStatement();
                break;
            case WHILE:
                nodeWhileStatement();
                break;
            default:
                nodeRepeatStatement();
        }
        consumeToken(ENDCYCLE);
        builder.closeNode();
    }

    private void nodeForStatement() throws ParseException {
        Node node = new NodeForStatement(lookup());
        builder.openNode(node);
        consumeToken(FOR);
        nodeLeftValue();
        consumeToken(FROM);
        nodeAdditiveExpression();
        consumeToken(TO);
        nodeAdditiveExpression();
        nodeBlock();
        builder.closeNode();
    }

    private void nodeWhileStatement() throws ParseException {
        Node node = new NodeWhileStatement(lookup());
        builder.openNode(node);
        consumeToken(WHILE);
        nodeExpression();
        nodeBlock();
        builder.closeNode();
    }

    private void nodeRepeatStatement() throws ParseException {
        Node node = new NodeRepeatStatement(lookup());
        builder.openNode(node);
        nodeBlock();
        consumeToken(WHILE);
        nodeExpression();
        builder.closeNode();
    }

    private void nodeReturnStatement() throws ParseException {
        Node node = new NodeReturnStatement(lookup());
        builder.openNode(node);
        consumeToken(RETURN);
        if (!checkToken(SEMICOLON)) {
            nodeExpression();
        }
        builder.closeNode();
    }

    private void nodeIfStatement() throws ParseException {
        Node node = new NodeIfStatement(lookup());
        builder.openNode(node);
        consumeToken(IF);
        nodeExpression();
        consumeToken(THEN);
        nodeBlock();
        if (checkAndConsumeToken(ELSE)) {
            nodeBlock();
        }
        consumeToken(ALL);
        builder.closeNode();
    }

    private void nodeCallFunction() throws ParseException {
        Node node = new NodeCallFunction(lookup());
        builder.openNode(node);
        nodeIdentifier();
        consumeToken(OPARENTHESIS);
        if (!checkAndConsumeToken(CPARENTHESIS)) {
            do {
                nodeExpression();
            } while (checkAndConsumeToken(COMMA));
            consumeToken(CPARENTHESIS);
        }
        builder.closeNode();
    }

    private void nodeLeftValue() throws ParseException {
        Node node = new NodeLeftValue(lookup());
        builder.openNode(node);
        do {
            nodeIdentifier();
            while (nodeBracketsInExpression()) {
            }
        } while (checkAndConsumeToken(DOT));
        builder.closeNode();
    }

    private boolean nodeBracketsInExpression() throws ParseException {
        Node node = new NodeBracketsInExpression(lookup());
        builder.openNode(node);
        boolean result = false;
        if (checkAndConsumeToken(OBRACKET)) {
            do {
                nodeAdditiveExpression();
            } while (checkAndConsumeToken(COMMA));
            consumeToken(CBRACKET);
            result = true;
        }
        builder.closeNode();
        return result;
    }

    private void nodeAssignment() throws ParseException {
        Node node = new NodeAssignment(lookup());
        builder.openNode(node);
        nodeLeftValue();
        consumeToken(ASSIGN);
        nodeExpression();
        builder.closeNode();
    }

    private void nodeExpression() throws ParseException {
        Node node = new NodeExpression(lookup());
        builder.openNode(node);
        do {
            nodeConditionalAndExpression();
        } while (checkAndConsumeToken(OR));
        builder.closeNode();
    }

    private void nodeConditionalAndExpression() throws ParseException {
        Node node = new NodeConditionalAndExpression(lookup());
        builder.openNode(node);
        do {
            nodeRelationExpression();
        } while (checkAndConsumeToken(AND));
        builder.closeNode();
    }

    private void nodeRelationExpression() throws ParseException {
        Node node = new NodeRelationExpression(lookup());
        builder.openNode(node);
        nodeAdditiveExpression();
        if (checkRelationSignToken()) {
            nodeRelationSign();
            nodeAdditiveExpression();
        }
        builder.closeNode();
    }

    private void nodeRelationSign() throws ParseException {
        Node node = new NodeRelationSign(lookup());
        builder.openNode(node);
        getNextToken();
        builder.closeNode();
    }

    private void nodeAdditiveExpression() throws ParseException {
        Node node = new NodeAdditiveExpression(lookup());
        builder.openNode(node);
        nodeMultiplicativeExpression();
        while (checkAdditiveSignToken()) {
            nodeAdditiveSign();
            nodeMultiplicativeExpression();
        }
        builder.closeNode();
    }

    private void nodeAdditiveSign() throws ParseException {
        Node node = new NodeAdditiveSign(lookup());
        builder.openNode(node);
        getNextToken();
        builder.closeNode();
    }

    private void nodeMultiplicativeExpression() throws ParseException {
        Node node = new NodeMultiplicativeExpression(lookup());
        builder.openNode(node);
        nodeUnaryExpression();
        while (checkMultiplicativeSignToken()) {
            nodeMultiplicativeSign();
            nodeUnaryExpression();
        }
        builder.closeNode();
    }

    private void nodeMultiplicativeSign() throws ParseException {
        Node node = new NodeMultiplicativeSign(lookup());
        builder.openNode(node);
        getNextToken();
        builder.closeNode();
    }

    private void nodeUnaryExpression() throws ParseException {
        Node node = new NodeUnaryExpression(lookup());
        builder.openNode(node);
        nodeUnarySign();
        nodeUnaryValue();
        builder.closeNode();
    }

    private void nodeUnarySign() throws ParseException {
        Node node = new NodeUnarySign(lookup());
        builder.openNode(node);
        if (checkUnarySignToken()) {
            getNextToken();
        }
        builder.closeNode();
    }

    private void nodeUnaryValue() throws ParseException {
        Node node = new NodeUnaryValue(lookup());
        builder.openNode(node);
        switch (lookup().getKind()) {
            case INTEGER_LITERAL:
            case FLOATING_POINT_LITERAL:
            case CHARACTER_LITERAL:
            case STRING_LITERAL:
            case TRUE:
            case FALSE:
                getNextToken();
                break;
            case OPARENTHESIS:
                getNextToken();
                nodeExpression();
                consumeToken(CPARENTHESIS);
                break;
            case IDENTIFIER:
                Token temp = current;
                getNextToken();
                boolean function = checkToken(OPARENTHESIS);
                current = temp;
                if (function) {
                    nodeCallFunction();
                } else {
                    do {
                        nodeIdentifier();
                        while (nodeBracketsInExpression()) {
                        }
                    } while (checkAndConsumeToken(DOT));
                }
                break;
            default:
                throw new ParseException(lookup().getKind(),
                        lookup().getPosition());
        }
        builder.closeNode();
    }
}
