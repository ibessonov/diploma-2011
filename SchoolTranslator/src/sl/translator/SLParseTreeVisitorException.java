package sl.translator;

import sl.parser.TokenPosition;
import sl.parser.nodes.ParseTreeVisitorException;

class SLParseTreeVisitorException extends ParseTreeVisitorException {

    public SLParseTreeVisitorException(TokenPosition position,
            SLParseTreeVisitorError error, Object... fields) {
        super(initialize(String.format(error.toString(), (Object[]) fields),
                position), position);
    }

    private static String initialize(
            String message,TokenPosition position) {
        StringBuilder builder = new StringBuilder();
        if(position == null) {
            position = new TokenPosition(0, 0, 0, 0, 0, 0);
        }
        builder.append(
                String.format("Ошибка в строке %d, столбце %d",
                position.getBeginLine(), position.getBeginColumn()));
        builder.append(EOL);
        builder.append(message);
        return builder.toString();
    }
}
