package sl.elements.character;

import sl.elements.DefaultType;
import sl.elements.StackElement;
import sl.elements.StackElementException;
import sl.elements.Type;
import sl.elements.string.StringType;
import sl.operations.binary.BinaryOperation;

public final class CharacterType extends DefaultType {

    private static final Type instance = new CharacterType();

    private CharacterType() {
    }

    public static Type get() {
        return instance;
    }

    @Override
    public StackElement instance() {
        return new CharacterElement('\0');
    }

    @Override
    public Type convert() {
        return StringType.get();
    }

    @Override
    public Type operationResult(BinaryOperation id) throws StackElementException {
        try {
            return CharacterBinaryOperation.valueOf(id.name()).type();
        } catch (IllegalArgumentException e) {
            return super.operationResult(id);
        }
    }

    @Override
    public String toString() {
        return "лит";
    }
}
