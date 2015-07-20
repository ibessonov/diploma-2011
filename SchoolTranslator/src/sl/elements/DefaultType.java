package sl.elements;

import sl.operations.binary.BinaryOperation;
import sl.operations.unary.UnaryOperation;

public abstract class DefaultType implements Type {

    @Override
    public boolean equals(Type type) {
        return this == type;
    }

    @Override
    public Type operationResult(UnaryOperation id)
            throws StackElementException {
        throw new StackElementException(
                StackElementError.TYPE_OPERATION_NOT_SUPPORTED,
                this.toString(), id.toString());
    }

    @Override
    public Type operationResult(BinaryOperation id)
            throws StackElementException {
        throw new StackElementException(
                StackElementError.TYPE_OPERATION_NOT_SUPPORTED,
                this.toString(), id.toString());
    }

    @Override
    public Type convert() throws StackElementException {
        throw new StackElementException(
                StackElementError.TYPE_CONVERTION_NOT_SUPPORTED,
                this.toString());
    }

    @Override
    public Type convert(Type type) throws StackElementException {
        Type other = type;
        while (!this.equals(other)) {
            other = other.convert();
        }
        return other;
    }

    @Override
    public StackElement convert(StackElement instance)
            throws StackElementException {
        StackElement other = instance;
        while (!this.equals(other.type())) {
            other = other.convert();
        }
        return other;
    }
}
