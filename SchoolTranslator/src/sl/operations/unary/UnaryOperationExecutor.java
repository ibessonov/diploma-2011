package sl.operations.unary;

import sl.elements.StackElement;
import sl.elements.StackElementException;

public abstract class UnaryOperationExecutor<S extends StackElement> {

    protected S src;

    public UnaryOperationExecutor(S src) {
        this.src = src;
    }

    public abstract StackElement apply() throws StackElementException;
}
