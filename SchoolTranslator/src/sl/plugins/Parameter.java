package sl.plugins;

import sl.elements.Type;

public class Parameter {

    private Type type;
    private boolean result;

    public Parameter(Type type) {
        this(type, false);
    }

    public Parameter(Type type, boolean result) {
        this.type = type;
        this.result = result;
    }

    public Type type() {
        return type;
    }

    public boolean isResult() {
        return result;
    }
}
