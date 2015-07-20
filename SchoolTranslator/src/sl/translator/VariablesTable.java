package sl.translator;

import java.util.HashMap;
import java.util.Map;
import sl.elements.Type;
import sl.parser.Token;

final class VariablesTable {

    private Map<String, Variable> table = new HashMap<String, Variable>();

    public boolean contains(String name) {
        return table.containsKey(name);
    }

    public Variable add(Token token, Type type)
            throws SLParseTreeVisitorException {
        String name = token.getImage();
        if (contains(name)) {
            throw new SLParseTreeVisitorException(token.getPosition(),
                    SLParseTreeVisitorError.VARIABLE_ALREADY_EXISTS, name);
        }
        Variable variable = new Variable(count(), type);
        table.put(name, variable);
        return variable;
    }

    public Variable get(Token token) throws SLParseTreeVisitorException {
        String name = token.getImage();
        if (!contains(name)) {
            throw new SLParseTreeVisitorException(token.getPosition(),
                    SLParseTreeVisitorError.VARIABLE_NOT_FOUND, name);
        }
        return table.get(name);
    }

    public int count() {
        return table.size();
    }

    public void clear() {
        table.clear();
    }
}