package sl.operations;

import sl.elements.StackElement;
import sl.elements.StackElementException;
import sl.operations.binary.BinaryOperation;
import sl.operations.unary.UnaryOperation;

public final class ExecuteOperation {

    private ExecuteOperation() {
    }

    public static StackElement binary(BinaryOperation id, StackElement left, StackElement right)
            throws StackElementException {
        try {
            return left.getBinaryOperation(id).applyRight(right);
        } catch (StackElementException e) {
            return right.getBinaryOperation(id).applyLeft(left);
        }
    }

    public static StackElement unary(UnaryOperation id, StackElement element)
            throws StackElementException {
        return element.getUnaryOperation(id).apply();
    }
}
