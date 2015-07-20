package sl.elements.character;

import sl.operations.binary.BinaryOperation;
import sl.operations.binary.BinaryOperationExecutor;
import java.io.IOException;
import java.util.Scanner;
import sl.elements.DefaultElement;
import sl.elements.StackElement;
import sl.elements.StackElementException;
import sl.elements.string.StringElement;
import sl.elements.Type;

public final class CharacterElement extends DefaultElement {

    private char value = 0;

    public CharacterElement(char c) {
        value = c;
    }

    public char value() {
        return value;
    }

    public void setValue(char c) {
        value = c;
    }

    @Override
    public Type type() {
        return CharacterType.get();
    }

    @Override
    public void read(Scanner scanner) throws StackElementException {
        value = scanner.next(".").charAt(0);
    }

    @Override
    public void write(Appendable writer) throws IOException {
        writer.append(this.toString());
    }

    @Override
    public void assign(StackElement elem) throws StackElementException {
        CharacterElement otherChar = (CharacterElement)type().convert(elem);
        value = otherChar.value;
    }

    @Override
    public StackElement convert() {
        return new StringElement(String.valueOf(value));
    }

    @Override
    public boolean equals(StackElement elem) throws StackElementException {
        try {
            CharacterElement elemChar = (CharacterElement)type().convert(elem);
            return (value == elemChar.value);
        } catch (StackElementException ex) {
            StackElement other = elem.type().convert(this);
            return elem.equals(other);
        }
    }

    @Override
    public StackElement clone() {
        return new CharacterElement(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public BinaryOperationExecutor getBinaryOperation(BinaryOperation id) throws StackElementException {
        try {
            return CharacterBinaryOperation.valueOf(id.name()).get(this);
        } catch (IllegalArgumentException e) {
            return super.getBinaryOperation(id);
        }
    }
}
