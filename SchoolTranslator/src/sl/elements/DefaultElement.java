package sl.elements;

import java.io.IOException;
import java.util.Scanner;
import sl.operations.binary.BinaryOperation;
import sl.operations.binary.BinaryOperationExecutor;
import sl.operations.unary.UnaryOperation;
import sl.operations.unary.UnaryOperationExecutor;

public abstract class DefaultElement implements StackElement {

    @Override
    public void assign(StackElement elem) throws StackElementException {
        throw new StackElementException(
                StackElementError.TYPE_ASSIGNMENT_NOT_SUPPORTED, type().toString());
    }

    @Override
    public void read(Scanner scanner) throws StackElementException, IOException {
        throw new StackElementException(
                StackElementError.TYPE_READ_NOT_SUPPORTED, type().toString());
    }

    @Override
    public void write(Appendable writer) throws StackElementException, IOException {
        throw new StackElementException(
                StackElementError.TYPE_WRITE_NOT_SUPPORTED, type().toString());
    }

    @Override
    public UnaryOperationExecutor getUnaryOperation(UnaryOperation id)
            throws StackElementException {
        throw new StackElementException(
                StackElementError.TYPE_OPERATION_NOT_SUPPORTED,
                type().toString(), id.toString());
    }

    @Override
    public BinaryOperationExecutor getBinaryOperation(BinaryOperation id)
            throws StackElementException {
        throw new StackElementException(
                StackElementError.TYPE_OPERATION_NOT_SUPPORTED,
                type().toString(), id.toString());
    }

    @Override
    public StackElement convert() throws StackElementException {
        throw new StackElementException(
                StackElementError.TYPE_CONVERTION_NOT_SUPPORTED,
                type().toString());
    }

    @Override
    public abstract StackElement clone();
}
