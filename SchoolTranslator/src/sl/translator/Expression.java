package sl.translator;

import sl.elements.StackElement;
import sl.elements.Type;

class Expression {

    private StackElement elem;
    private Type type;
    private boolean lval;

    public Expression(StackElement elem) {
        this(elem, elem.type(), false);
    }

    public Expression(Type type, boolean lval) {
        this(null, type, lval);
    }

    private Expression(StackElement elem, Type type, boolean lval) {
        this.elem = elem;
        this.type = type;
        this.lval = lval;
    }

    public Type type() {
        return type;
    }

    public StackElement element() {
        return elem;
    }

    public boolean isConstant() {
        return (elem != null);
    }

    public boolean isLeftValue() {
        return lval;
    }
}
