package sl.elements.string;

import sl.operations.binary.BinaryOperation;
import sl.operations.binary.BinaryOperationExecutor;
import java.io.IOException;
import java.util.Scanner;
import sl.elements.DefaultElement;
import sl.elements.StackElement;
import sl.elements.StackElementException;
import sl.elements.Type;

public final class StringElement extends DefaultElement {

    private String value = "";

    public StringElement(String string) {
        this.value = string;
    }

    public String value() {
        return value;
    }

    public void setValue(String string) {
        this.value = string;
    }

    @Override
    public Type type() {
        return StringType.get();
    }

    @Override
    public void read(Scanner scanner) throws StackElementException {
        value = scanner.nextLine();
    }

    @Override
    public void write(Appendable writer) throws IOException {
        writer.append(value);
    }

    @Override
    public void assign(StackElement elem) throws StackElementException {
        value = elem.toString();
    }

    @Override
    public boolean equals(StackElement elem) throws StackElementException {
        StringElement elemString = (StringElement) ((StringType) StringType.get()).convert(elem);
        return (value.equals(elemString.value));
    }

    @Override
    public StackElement clone() {
        return new StringElement(value);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public BinaryOperationExecutor getBinaryOperation(BinaryOperation id) throws StackElementException {
        try {
            return StringBinaryOperation.valueOf(id.name()).get(this);
        } catch (IllegalArgumentException e) {
            return super.getBinaryOperation(id);
        }
    }
}
