package sl.operations;

import sl.elements.StackElement;
import sl.elements.Type;

public interface OperationGetter<S extends StackElement, E> {

    public Type type();

    public E get(S element);
}
