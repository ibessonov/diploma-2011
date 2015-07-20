package sl.elements;

public class StackElementException extends Exception {

    public StackElementException(StackElementError s) {
        super(s.toString());
    }

    public StackElementException(StackElementError s, Object... fields) {
        super(String.format(s.toString(), fields));
    }
}
