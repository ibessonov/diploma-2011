package sl.parser.nodes;

import sl.parser.AbstractParseException;
import sl.parser.TokenPosition;

public class ParseTreeVisitorException extends AbstractParseException {

    public ParseTreeVisitorException(String msg, TokenPosition position) {
        super(msg, position);
    }
}
