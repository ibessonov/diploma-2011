package sl.elements.integer;

import sl.elements.DefaultType;
import sl.elements.StackElement;
import sl.elements.StackElementException;
import sl.elements.Type;
import sl.elements.real.RealType;
import sl.operations.binary.BinaryOperation;
import sl.operations.unary.UnaryOperation;

public final class IntegerType extends DefaultType {

    private static final Type instance = new IntegerType();

    private IntegerType() {
    }

    public static Type get() {
        return instance;
    }

    @Override
    public StackElement instance() {
        return new IntegerElement(0);
    }

    @Override
    public Type convert() throws StackElementException {
        return RealType.get();
    }

    @Override
    public Type operationResult(UnaryOperation id) throws StackElementException {
        try {
            return IntegerUnaryOperation.valueOf(id.name()).type();
        } catch (IllegalArgumentException e) {
            return super.operationResult(id);
        }
    }

    @Override
    public Type operationResult(BinaryOperation id) throws StackElementException {
        try {
            return IntegerBinaryOperation.valueOf(id.name()).type();
        } catch (IllegalArgumentException e) {
            return super.operationResult(id);
        }
    }

    @Override
    public String toString() {
        return "цел";
    }
}
