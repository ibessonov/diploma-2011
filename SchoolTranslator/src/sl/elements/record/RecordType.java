package sl.elements.record;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import sl.elements.DefaultType;

import sl.elements.StackElement;
import sl.elements.Type;

public final class RecordType extends DefaultType {

    private Map<String, Type> fields;
    private String name;

    public RecordType(String name) {
        this.name = name;
        fields = new HashMap<String, Type>();
    }

    public boolean isIn(String name) {
        return fields.containsKey(name);
    }

    public void addField(String name, Type type) {
        fields.put(name, type);
    }

    public Type getFieldType(String name) {
        return fields.get(name);
    }

    public Set<String> getFields() {
        return fields.keySet();
    }

    @Override
    public StackElement instance() {
        return new RecordElement(this);
    }

    @Override
    public boolean equals(Type type) {
        if (!(type instanceof RecordType)) {
            return false;
        }
        RecordType typeRecord = (RecordType) type;
        return fields.equals(typeRecord.fields);
    }

    @Override
    public String toString() {
        return name;
    }
}
