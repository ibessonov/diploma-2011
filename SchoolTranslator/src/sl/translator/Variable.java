package sl.translator;

import sl.elements.Type;
import sl.plugins.Parameter;

/**
 *
 * @author Полевая Евгения
 */
final class Variable extends Parameter {

    private int offset;

    public Variable(Type type) {
        this(0, type);
    }

    public Variable(int offset, Type type) {
        this(offset, type, false);
    }

    public Variable(int offset, Type type, boolean result) {
        super(type, result);
        this.offset = offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }
}
