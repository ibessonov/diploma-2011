package sl.elements;

import java.io.IOException;
import java.util.Scanner;
import sl.operations.unary.UnaryOperation;
import sl.operations.unary.UnaryOperationExecutor;
import sl.operations.binary.BinaryOperation;
import sl.operations.binary.BinaryOperationExecutor;

public interface StackElement extends Cloneable,
        Convertable<StackElement, StackElementException> {

    public Type type();

    public StackElement clone();

    public boolean equals(StackElement elem) throws StackElementException;

    public void assign(StackElement elem) throws StackElementException;

    public void read(Scanner scanner) throws StackElementException, IOException;

    public void write(Appendable writer) throws StackElementException, IOException;

    public UnaryOperationExecutor getUnaryOperation(UnaryOperation id) throws StackElementException;

    public BinaryOperationExecutor getBinaryOperation(BinaryOperation id) throws StackElementException;
}
