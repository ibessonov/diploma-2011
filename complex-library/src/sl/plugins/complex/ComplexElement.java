package sl.plugins.complex;

import java.io.IOException;
import sl.elements.DefaultElement;
import sl.elements.StackElement;
import sl.elements.StackElementException;
import sl.elements.Type;
import sl.operations.binary.BinaryOperation;
import sl.operations.binary.BinaryOperationExecutor;
import sl.operations.unary.UnaryOperation;
import sl.operations.unary.UnaryOperationExecutor;

public class ComplexElement extends DefaultElement {

    private double real;
    private double image;
    public static final ComplexElement IMAGINARY_UNIT = new ComplexElement(0, 1);

    public ComplexElement(double real, double image) {
        this.real = real;
        this.image = image;
    }

    public double real() {
        return real;
    }

    public double image() {
        return image;
    }

    @Override
    public Type type() {
        return ComplexType.get();
    }

    @Override
    public void write(Appendable writer) throws IOException {
        writer.append(this.toString());
    }

    @Override
    public void assign(StackElement elem) throws StackElementException {
        ComplexElement other = (ComplexElement) type().convert(elem);
        real = other.real;
        image = other.image;
    }

    @Override
    public boolean equals(StackElement elem) throws StackElementException {
        try {
            ComplexElement other = (ComplexElement) type().convert(elem);
            return (real == other.real && image == other.image);
        } catch (StackElementException ex) {
            StackElement other = elem.type().convert(this);
            return elem.equals(other);
        }
    }

    @Override
    public StackElement clone() {
        return new ComplexElement(real, image);
    }

    @Override
    public String toString() {
        if (real == 0) {
            if (image == 0) {
                return "0";
            } else if (image == 1) {
                return "i";
            } else if (image == -1) {
                return "-i";
            } else {
                return String.valueOf(image).concat("*i");
            }
        } else {
            if (image == 0) {
                return String.valueOf(real);
            } else if (image == 1) {
                return String.valueOf(real).concat("+i");
            } else if (image == -1) {
                return String.valueOf(real).concat("-i");
            } else if (image > 0) {
                return String.valueOf(real).concat("+").
                        concat(String.valueOf(image)).concat("*i");
            } else {
                return String.valueOf(real).
                        concat(String.valueOf(image)).concat("*i");
            }
        }
    }

    @Override
    public UnaryOperationExecutor getUnaryOperation(UnaryOperation id)
            throws StackElementException {
        try {
            return ComplexUnaryOperation.valueOf(id.name()).get(this);
        } catch (IllegalArgumentException e) {
            return super.getUnaryOperation(id);
        }
    }

    @Override
    public BinaryOperationExecutor getBinaryOperation(BinaryOperation id)
            throws StackElementException {
        try {
            return ComplexBinaryOperation.valueOf(id.name()).get(this);
        } catch (IllegalArgumentException e) {
            return super.getBinaryOperation(id);
        }
    }
}
