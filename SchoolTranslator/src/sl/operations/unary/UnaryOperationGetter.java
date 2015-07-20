package sl.operations.unary;

import sl.elements.StackElement;
import sl.operations.OperationGetter;

public interface UnaryOperationGetter<S extends StackElement>
        extends OperationGetter<S, UnaryOperationExecutor<S>> {
}
