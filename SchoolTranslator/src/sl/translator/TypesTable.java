package sl.translator;

import java.util.HashMap;
import java.util.Map;
import sl.elements.Type;
import sl.parser.Token;
import sl.plugins.TypesFactory;

final class TypesTable {

    private Map<String, Type> table = new HashMap<String, Type>();

    public boolean contains(String name) {
        return table.containsKey(name)
                || (TypesFactory.instance().get(name) != null);
    }

    public void add(Token token, Type type)
            throws SLParseTreeVisitorException {
        String name = token.getImage();
        if (contains(name)) {
            throw new SLParseTreeVisitorException(token.getPosition(),
                    SLParseTreeVisitorError.TYPE_ALREADY_EXISTS, name);
        }
        table.put(name, type);
    }

    public Type get(Token token) throws SLParseTreeVisitorException {
        String name = token.getImage();
        if (!contains(name)) {
            throw new SLParseTreeVisitorException(token.getPosition(),
                    SLParseTreeVisitorError.TYPE_NOT_FOUND, name);
        }
        return table.containsKey(name)
                ? table.get(name)
                : TypesFactory.instance().get(name);
    }
}
