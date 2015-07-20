package sl.elements.integer;

import sl.operations.unary.UnaryOperationExecutor;
import sl.operations.binary.BinaryOperation;
import sl.operations.binary.BinaryOperationExecutor;
import java.io.IOException;
import java.util.Scanner;
import sl.elements.DefaultElement;
import sl.elements.real.RealElement;
import sl.elements.StackElement;
import sl.elements.StackElementException;
import sl.elements.Type;
import sl.operations.unary.UnaryOperation;

public final class IntegerElement extends DefaultElement {

    private int value = 0;

    public IntegerElement(int n) {
        value = n;
    }

    public int value() {
        return value;
    }

    public void setValue(int n) {
        value = n;
    }

    @Override
    public Type type() {
        return IntegerType.get();
    }

    @Override
    public void read(Scanner scanner) throws StackElementException {
        value = scanner.nextInt();
    }

    @Override
    public void write(Appendable writer) throws IOException {
        writer.append(this.toString());
    }

    @Override
    public void assign(StackElement elem) throws StackElementException {
        IntegerElement otherInt = (IntegerElement) type().convert(elem);
        value = otherInt.value;
    }

    @Override
    public StackElement convert() {
        return new RealElement(value);
    }

    @Override
    public boolean equals(StackElement elem) throws StackElementException {
        try {
            IntegerElement elemInt = (IntegerElement) type().convert(elem);
            return (value == elemInt.value);
        } catch (StackElementException ex) {
            StackElement other = elem.type().convert(this);
            return elem.equals(other);
        }
    }

    @Override
    public StackElement clone() {
        return new IntegerElement(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public UnaryOperationExecutor getUnaryOperation(UnaryOperation id) throws StackElementException {
        try {
            return IntegerUnaryOperation.valueOf(id.name()).get(this);
        } catch (IllegalArgumentException e) {
            return super.getUnaryOperation(id);
        }
    }

    @Override
    public BinaryOperationExecutor getBinaryOperation(BinaryOperation id) throws StackElementException {
        try {
            return IntegerBinaryOperation.valueOf(id.name()).get(this);
        } catch (IllegalArgumentException e) {
            return super.getBinaryOperation(id);
        }
    }
}
