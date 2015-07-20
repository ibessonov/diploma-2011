package sl.elements.record;

import java.util.HashMap;
import java.util.Map;
import sl.elements.DefaultElement;
import sl.elements.StackElement;
import sl.elements.StackElementException;
import sl.elements.Type;

public class RecordElement extends DefaultElement {

    private RecordType type;
    private Map<String, StackElement> fields;

    public RecordElement(RecordType type) {
        this.type = type;
        fields = new HashMap<String, StackElement>();
        for (String field : type.getFields()) {
            fields.put(field, type.getFieldType(field).instance());
        }
    }

    private RecordElement(RecordType type, Map<String, StackElement> fields) {
        this.type = type;
        this.fields = fields;
    }

    public StackElement get(String field) {
        return fields.get(field);
    }

    @Override
    public Type type() {
        return type;
    }

    @Override
    public void assign(StackElement elem) throws StackElementException {
        RecordElement other = (RecordElement)type().convert(elem);
        this.fields = other.fields;
    }

    @Override
    public boolean equals(StackElement elem) throws StackElementException {
        try {
            RecordElement other = (RecordElement)type().convert(elem);
            return this.fields.equals(other.fields);
        } catch (StackElementException ex) {
            StackElement other = elem.type().convert(this);
            return elem.equals(other);
        }
    }

    @Override
    public StackElement clone() {
        Map<String, StackElement> map = new HashMap<String, StackElement>();
        for (String field : fields.keySet()) {
            map.put(field, fields.get(field));
        }
        return new RecordElement(type, map);
    }

    @Override
    public String toString() {
        return "";
    }
}
