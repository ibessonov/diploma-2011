package sl.translator;

import sl.elements.StackElement;
import sl.elements.bool.BooleanElement;
import sl.elements.character.CharacterElement;
import sl.elements.integer.IntegerElement;
import sl.elements.real.RealElement;
import sl.elements.string.StringElement;
import sl.parser.ParseError;
import sl.parser.ParseException;
import sl.parser.Token;
import sl.parser.nodes.ParseTreeVisitorException;

final class ConstantsParser {

    static StackElement parse(Token value) throws ParseTreeVisitorException {
        try {
            switch (value.getKind()) {
                case TRUE:
                    return BooleanElement.TRUE;
                case FALSE:
                    return BooleanElement.FALSE;
                case INTEGER_LITERAL:
                    return new IntegerElement(Integer.valueOf(value.getImage()));
                case FLOATING_POINT_LITERAL:
                    return new RealElement(Double.valueOf(value.getImage()));
                case CHARACTER_LITERAL:
                    return new CharacterElement(unEscape(value.getImage()).charAt(0));
                case STRING_LITERAL:
                    return new StringElement(unEscape(value.getImage()));
                default:
                    return null;
            }
        } catch (NumberFormatException ex) {
            ParseException parseEx = new ParseException(ParseError.WRONG_NUMBER_FORMAT, value.getPosition());
            throw new ParseTreeVisitorException(parseEx.getMessage(), value.getPosition());
        }
    }

    private static String unEscape(String image) {
        String temp = image.substring(1, image.length() - 1);
        temp = temp.replaceAll("\\\\\\\\", "\\\\@");
        temp = temp.replaceAll("\\\\н", "\n");
        temp = temp.replaceAll("\\\\т", "\t");
        temp = temp.replaceAll("\\\\'", "'");
        temp = temp.replaceAll("\\\\\"", "\"");
        temp = temp.replaceAll("\\\\@", "\\\\");
        return temp;
    }
}
