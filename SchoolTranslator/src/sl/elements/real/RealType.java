package sl.elements.real;

import sl.elements.DefaultType;
import sl.elements.StackElement;
import sl.elements.StackElementException;
import sl.elements.Type;
import sl.operations.binary.BinaryOperation;
import sl.operations.unary.UnaryOperation;

public final class RealType extends DefaultType {

    private static final Type instance = new RealType();

    private RealType() {
    }

    public static Type get() {
        return instance;
    }

    @Override
    public StackElement instance() {
        return new RealElement(0.);
    }

    @Override
    public Type operationResult(UnaryOperation id) throws StackElementException {
        try {
            return RealUnaryOperation.valueOf(id.name()).type();
        } catch (IllegalArgumentException e) {
            return super.operationResult(id);
        }
    }

    @Override
    public Type operationResult(BinaryOperation id) throws StackElementException {
        try {
            return RealBinaryOperation.valueOf(id.name()).type();
        } catch (IllegalArgumentException e) {
            return super.operationResult(id);
        }
    }

    @Override
    public String toString() {
        return "вещ";
    }
}
