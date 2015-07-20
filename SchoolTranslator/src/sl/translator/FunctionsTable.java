package sl.translator;

import java.util.HashMap;
import java.util.Map;
import sl.parser.Token;
import sl.plugins.Function;
import sl.plugins.FunctionsFactory;

final class FunctionsTable {

    public static final int EXTERNAL_FUNCTION = -1;
    private Map<String, Function> functions = new HashMap<String, Function>();
    private Map<String, Integer> entrances = new HashMap<String, Integer>();

    public boolean contains(String name) {
        return functions.containsKey(name)
                || (FunctionsFactory.instance().get(name) != null);
    }

    public void add(Token token, Function function, int entrance)
            throws SLParseTreeVisitorException {
        String name = token.getImage();
        if (contains(name)) {
            throw new SLParseTreeVisitorException(token.getPosition(),
                    SLParseTreeVisitorError.FUNCTION_ALREADY_EXISTS, name);
        }
        functions.put(name, function);
        entrances.put(name, entrance);
    }

    public Function getFunction(Token token) throws SLParseTreeVisitorException {
        String name = token.getImage();
        if (!contains(name)) {
            throw new SLParseTreeVisitorException(token.getPosition(),
                    SLParseTreeVisitorError.FUNCTION_NOT_FOUND, name);
        }
        return functions.containsKey(name)
                ? functions.get(name)
                : FunctionsFactory.instance().get(name);
    }

    public int getEntrance(Token token) throws SLParseTreeVisitorException {
        String name = token.getImage();
        if (!contains(name)) {
            throw new SLParseTreeVisitorException(token.getPosition(),
                    SLParseTreeVisitorError.FUNCTION_NOT_FOUND, name);
        }
        return entrances.containsKey(name)
                ? entrances.get(name)
                : EXTERNAL_FUNCTION;
    }

    public void clear() {
        functions.clear();
    }
}
