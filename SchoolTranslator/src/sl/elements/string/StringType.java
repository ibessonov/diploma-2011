package sl.elements.string;

import sl.elements.DefaultType;
import sl.elements.StackElement;
import sl.elements.StackElementException;
import sl.elements.Type;
import sl.operations.binary.BinaryOperation;

public final class StringType extends DefaultType {

    private static final Type instance = new StringType();

    private StringType() {
    }

    public static Type get() {
        return instance;
    }

    @Override
    public StackElement instance() {
        return new StringElement("");
    }

    @Override
    public Type operationResult(BinaryOperation id) throws StackElementException {
        try {
            return StringBinaryOperation.valueOf(id.name()).type();
        } catch (IllegalArgumentException e) {
            return super.operationResult(id);
        }
    }

    @Override
    public String toString() {
        return "стр";
    }
}
