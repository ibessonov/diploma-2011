package sl.operations.binary;

import sl.elements.StackElement;
import sl.elements.StackElementException;

public abstract class BinaryOperationExecutor<S extends StackElement> {

    protected S src;

    public BinaryOperationExecutor(S src) {
        this.src = src;
    }

    public StackElement applyLeft(StackElement left)
            throws StackElementException {
        S other = (S) src.type().convert(left);
        return apply(other, src);
    }

    public StackElement applyRight(StackElement right)
            throws StackElementException {
        S other = (S) src.type().convert(right);
        return apply(src, other);
    }

    protected abstract StackElement apply(S left, S right);
}
