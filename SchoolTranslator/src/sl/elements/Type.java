package sl.elements;

import sl.operations.binary.BinaryOperation;
import sl.operations.unary.UnaryOperation;

public interface Type extends
        Convertable<Type, StackElementException>,
        TypeConvertor, ElementConvertor {

    public boolean equals(Type type);

    public StackElement instance();

    public Type operationResult(UnaryOperation id)
            throws StackElementException;

    public Type operationResult(BinaryOperation id)
            throws StackElementException;
}
