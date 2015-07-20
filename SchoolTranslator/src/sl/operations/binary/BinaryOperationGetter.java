package sl.operations.binary;

import sl.elements.StackElement;
import sl.operations.OperationGetter;

public interface BinaryOperationGetter<S extends StackElement>
        extends OperationGetter<S, BinaryOperationExecutor<S>> {
}
