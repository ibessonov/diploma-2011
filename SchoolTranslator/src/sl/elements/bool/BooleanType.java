package sl.elements.bool;

import sl.elements.DefaultType;
import sl.elements.StackElement;
import sl.elements.StackElementException;
import sl.elements.Type;
import sl.operations.binary.BinaryOperation;
import sl.operations.unary.UnaryOperation;

public final class BooleanType extends DefaultType {

    private static final Type instance = new BooleanType();

    private BooleanType() {
    }

    public static Type get() {
        return instance;
    }

    @Override
    public StackElement instance() {
        return new BooleanElement(false);
    }

    @Override
    public Type operationResult(UnaryOperation id) throws StackElementException {
        try {
            return BooleanUnaryOperation.valueOf(id.name()).type();
        } catch (IllegalArgumentException e) {
            return super.operationResult(id);
        }
    }

    @Override
    public Type operationResult(BinaryOperation id) throws StackElementException {
        try {
            return BooleanBinaryOperation.valueOf(id.name()).type();
        } catch (IllegalArgumentException e) {
            return super.operationResult(id);
        }
    }

    @Override
    public String toString() {
        return "лог";
    }
}
