package sl.translator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sl.elements.Type;
import sl.parser.Token;
import sl.parser.nodes.ParseTreeVisitorException;
import sl.plugins.Function;
import sl.program.Command;
import sl.program.Program;

final class FunctionBuilder implements Function {

    private Type type;
    private String name;
    private List<String> paramNames = new ArrayList<String>();
    private Map<String, Variable> params = new HashMap<String, Variable>();
    private VariablesTable locals = new VariablesTable();
    private Program program = new Program();

    public FunctionBuilder(String name) {
        this(name, null);
    }

    public FunctionBuilder(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public Type getReturnType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

//Блок работы с параметрами
    public Variable addParameter(Token token, Type type, boolean result)
            throws ParseTreeVisitorException {
        assertNotParamName(token);
        for (Variable v : params.values()) {
            v.setOffset(v.getOffset() - 1);
        }
        paramNames.add(token.getImage());
        Variable variable = new Variable(-1, type, result);
        params.put(token.getImage(), variable);
        return variable;
    }

    @Override
    public Variable getParameter(int index) {
        return params.get(paramNames.get(index));
    }

    @Override
    public int getParamsCount() {
        return paramNames.size();
    }

//Блок работы с локальными переменными
    public Variable addVariable(Token token, Type type)
            throws ParseTreeVisitorException {
        assertNotParamName(token);
        return locals.add(token, type);
    }

    public Variable getVariable(Token token)
            throws ParseTreeVisitorException {
        if (params.containsKey(token.getImage())) {
            return params.get(token.getImage());
        }
        return locals.get(token);
    }

//Блок работы с кодом функции
    public int getCommandsCount() {
        return program.size();
    }

    @Override
    public Program getProgram() {
        return program;
    }

    public void addCommand(Command command) {
        program.append(command);
    }

//Вспомогательные вещи
    private void assertNotParamName(Token token)
            throws ParseTreeVisitorException {
        if (params.containsKey(name)) {
            throw new SLParseTreeVisitorException(token.getPosition(),
                    SLParseTreeVisitorError.VARIABLE_ALREADY_EXISTS, name);
        }
    }
}
