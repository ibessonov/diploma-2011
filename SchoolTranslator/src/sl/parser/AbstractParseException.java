package sl.parser;

public abstract class AbstractParseException extends Exception {

    protected static final String EOL = System.getProperty("line.separator", "\n");
    private TokenPosition position;

    public AbstractParseException(String msg, TokenPosition position) {
        super(msg);
        this.position = position;
    }

    public TokenPosition getPosition() {
        return position;
    }
}
