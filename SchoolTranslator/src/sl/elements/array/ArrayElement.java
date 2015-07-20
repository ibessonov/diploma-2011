package sl.elements.array;

import sl.elements.DefaultElement;
import sl.elements.StackElement;
import sl.elements.StackElementException;
import sl.elements.Type;

public class ArrayElement extends DefaultElement {

    private ArrayType type;
    private StackElement[] array;

    public ArrayElement(ArrayType type) {
        this.type = type;
        int count = type.count();
        array = new StackElement[count];
        Type innerType = type.type();
        for (int i = 0; i < count; ++i) {
            array[i] = innerType.instance();
        }
    }

    private ArrayElement(ArrayType type, StackElement[] array) {
        this.type = type;
        this.array = array;
    }

    public StackElement get(int i) {
        return array[i + type.base()];
    }

    @Override
    public Type type() {
        return type;
    }

    @Override
    public void assign(StackElement elem) throws StackElementException {
        ArrayElement other = (ArrayElement)type().convert(elem);
        this.array = other.array;
    }

    @Override
    public boolean equals(StackElement elem) throws StackElementException {
        try {
            ArrayElement other = (ArrayElement)type().convert(elem);
            for(int i = 0; i < array.length; ++i) {
                if(!array[i].equals(other.array[i])) {
                    return false;
                }
            }
            return true;
        } catch (StackElementException ex) {
            StackElement other = elem.type().convert(this);
            return elem.equals(other);
        }
    }

    @Override
    public StackElement clone() {
        StackElement[] clone = new StackElement[array.length];
        for(int i = 0; i < array.length; ++i) {
            clone[i] = array[i].clone();
        }
        return new ArrayElement(type, clone);
    }

    @Override
    public String toString() {
        return "";
    }
}
