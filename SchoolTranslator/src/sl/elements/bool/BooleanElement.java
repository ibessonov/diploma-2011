package sl.elements.bool;

import sl.operations.unary.UnaryOperation;
import sl.operations.unary.UnaryOperationExecutor;
import sl.operations.binary.BinaryOperation;
import sl.operations.binary.BinaryOperationExecutor;
import java.io.IOException;
import sl.elements.DefaultElement;
import sl.elements.StackElement;
import sl.elements.StackElementError;
import sl.elements.StackElementException;
import sl.elements.Type;

public final class BooleanElement extends DefaultElement {

    private boolean value = false;
    private static final String TRUE_STRING = "да";
    private static final String FALSE_STRING = "нет";
    public static final BooleanElement TRUE = new BooleanElement(true);
    public static final BooleanElement FALSE = new BooleanElement(false);

    public static BooleanElement get(boolean b) {
        return b ? TRUE : FALSE;
    }

    public BooleanElement(boolean b) {
        value = b;
    }

    public boolean value() {
        return value;
    }

    public void setValue(boolean b) {
        value = b;
    }

    @Override
    public Type type() {
        return BooleanType.get();
    }

    @Override
    public void write(Appendable writer) throws IOException {
        writer.append(this.toString());
    }

    @Override
    public void assign(StackElement elem) throws StackElementException {
        if (this == TRUE || this == FALSE) {
            throw new StackElementException(StackElementError.ASSIGNMENT_TO_CONSTANT);
        }
        BooleanElement otherBool = (BooleanElement)type().convert(elem);
        value = otherBool.value;
    }

    @Override
    public boolean equals(StackElement elem) throws StackElementException {
        try {
            BooleanElement elemBool = (BooleanElement)type().convert(elem);
            return (value == elemBool.value);
        } catch (StackElementException ex) {
            StackElement other = elem.type().convert(this);
            return elem.equals(other);
        }
    }

    @Override
    public StackElement clone() {
        return new BooleanElement(value);
    }

    @Override
    public String toString() {
        return value ? TRUE_STRING : FALSE_STRING;
    }

    @Override
    public UnaryOperationExecutor getUnaryOperation(UnaryOperation id) throws StackElementException {
        try {
            return BooleanUnaryOperation.valueOf(id.name()).get(this);
        } catch (IllegalArgumentException e) {
            return super.getUnaryOperation(id);
        }
    }

    @Override
    public BinaryOperationExecutor getBinaryOperation(BinaryOperation id) throws StackElementException {
        try {
            return BooleanBinaryOperation.valueOf(id.name()).get(this);
        } catch (IllegalArgumentException e) {
            return super.getBinaryOperation(id);
        }
    }
}
