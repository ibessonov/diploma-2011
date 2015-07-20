package sl.elements.real;

import sl.operations.unary.UnaryOperationExecutor;
import sl.operations.binary.BinaryOperation;
import sl.operations.binary.BinaryOperationExecutor;
import java.io.IOException;
import java.util.Scanner;
import sl.elements.DefaultElement;
import sl.elements.StackElement;
import sl.elements.StackElementException;
import sl.elements.string.StringElement;
import sl.elements.Type;
import sl.operations.unary.UnaryOperation;

public final class RealElement extends DefaultElement {

    private double value = 0;

    public RealElement(double n) {
        value = n;
    }

    public double value() {
        return value;
    }

    public void setValue(double n) {
        value = n;
    }

    @Override
    public Type type() {
        return RealType.get();
    }

    @Override
    public void read(Scanner scanner) throws StackElementException {
        value = scanner.nextDouble();
    }

    @Override
    public void write(Appendable writer) throws IOException {
        writer.append(this.toString());
    }

    @Override
    public void assign(StackElement elem) throws StackElementException {
        RealElement otherReal = (RealElement) type().convert(elem);
        value = otherReal.value;
    }

    @Override
    public boolean equals(StackElement elem) throws StackElementException {
        try {
            RealElement elemReal = (RealElement) type().convert(elem);
            return (value == elemReal.value);
        } catch (StackElementException ex) {
            StackElement other = elem.type().convert(this);
            return elem.equals(other);
        }
    }

    @Override
    public StackElement clone() {
        return new RealElement(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public UnaryOperationExecutor getUnaryOperation(UnaryOperation id)
            throws StackElementException {
        try {
            return RealUnaryOperation.valueOf(id.name()).get(this);
        } catch (IllegalArgumentException e) {
            return super.getUnaryOperation(id);
        }
    }

    @Override
    public BinaryOperationExecutor getBinaryOperation(BinaryOperation id)
            throws StackElementException {
        try {
            return RealBinaryOperation.valueOf(id.name()).get(this);
        } catch (IllegalArgumentException e) {
            return super.getBinaryOperation(id);
        }
    }
}
