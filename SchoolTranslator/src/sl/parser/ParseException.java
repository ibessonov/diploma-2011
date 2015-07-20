package sl.parser;

public class ParseException extends AbstractParseException {

    public ParseException(TokenKind current, TokenPosition position) {
        super(initialise(current, position), position);
    }

    public ParseException(ParseError error, TokenPosition position) {
        super(initialise(error, position), position);
    }

    private static String initialise(TokenKind current, TokenPosition position) {
        StringBuilder builder = new StringBuilder();
        builder.append(
                String.format("Синтаксическая ошибка в строке %d, столбце %d",
                position.getBeginLine(), position.getBeginColumn()));
        builder.append(EOL);
        builder.append(String.format("Обнаружено: [ %s ] ", current));
        return builder.toString();
    }

    private static String initialise(ParseError error, TokenPosition position) {
        StringBuilder builder = new StringBuilder();
        builder.append(
                String.format("Лексическая ошибка в строке %d, столбце %d",
                position.getBeginLine(), position.getBeginColumn()));
        builder.append(EOL);
        builder.append(error.toString());
        return builder.toString();
    }
}
