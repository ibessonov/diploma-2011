package sl.elements.array;

import java.util.ArrayList;
import java.util.List;
import sl.elements.DefaultType;

import sl.elements.StackElement;
import sl.elements.Type;

public final class ArrayType extends DefaultType {

    private List<Integer> from;
    private List<Integer> to;
    private Type type;
    private String name;
    private int base, count;

    public ArrayType(Type type, String name) {
        this.type = type;
        this.name = name;
        this.base = 0;
        this.count = 1;
        from = new ArrayList<Integer>();
        to = new ArrayList<Integer>();
    }

    public ArrayType(Type type) {
        this(type, null);
    }

    public Type type() {
        return type;
    }

    public int dimentions() {
        return from.size();
    }

    public void addDimention(int from, int to) {
        this.from.add(from);
        this.to.add(to);
        this.count *= (to - from + 1);
        this.base *= (to - from + 1);
        this.base -= from;
    }

    public int low(int index) {
        return from.get(index);
    }

    public int high(int index) {
        return to.get(index);
    }

    public int base() {
        return base;
    }

    public int count(int index) {
        return high(index) - low(index) + 1;
    }

    public int count() {
        return count;
    }

    @Override
    public boolean equals(Type type) {
        if (!(type instanceof ArrayType)) {
            return false;
        }
        ArrayType typeArray = (ArrayType) type;
        return this.type.equals(typeArray.type) &&
                from.equals(typeArray.from) && to.equals(typeArray.to);
    }

    @Override
    public StackElement instance() {
        return new ArrayElement(this);
    }

    @Override
    public String toString() {
        if(name != null) {
            return name;
        }
        StringBuilder builder = new StringBuilder(type.toString());
        builder.append(" таб[");
        int dims = dimentions();
        for(int i = 0; i < dims; ++i) {
            builder.append(from.get(i));
            builder.append(':');
            builder.append(to.get(i));
            builder.append(',');
        }
        builder.setCharAt(builder.length() - 1, ']');
        return builder.toString();
    }
}
