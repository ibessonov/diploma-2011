package sl.translator;

import static org.junit.Assert.*;

import org.junit.Test;

import sl.elements.StackElement;
import sl.elements.StackElementException;
import sl.elements.character.CharacterElement;
import sl.elements.integer.IntegerElement;
import sl.elements.real.RealElement;
import sl.elements.string.StringElement;
import sl.parser.Token;
import sl.parser.TokenKind;
import sl.parser.nodes.ParseTreeVisitorException;

public class ConstantsParserTest {

    @Test
    public void testParseString() {
        StackElement[] elements = {
            new RealElement(1.0),
            new IntegerElement(1),
            new CharacterElement('a'),
            new CharacterElement('\n'),
            new CharacterElement('\t'),
            new CharacterElement('\''),
            new CharacterElement('\\'),
            new StringElement("string"),
            new StringElement("\n"),
            new StringElement("\t"),
            new StringElement("\""),
            new StringElement("\\")
        };
        Token[] tokens = {
            new Token(TokenKind.FLOATING_POINT_LITERAL, "1.0", null),
            new Token(TokenKind.INTEGER_LITERAL, "1", null),
            new Token(TokenKind.CHARACTER_LITERAL, "'a'", null),
            new Token(TokenKind.CHARACTER_LITERAL, "'\\н'", null),
            new Token(TokenKind.CHARACTER_LITERAL, "'\\т'", null),
            new Token(TokenKind.CHARACTER_LITERAL, "'\\''", null),
            new Token(TokenKind.CHARACTER_LITERAL, "'\\\\'", null),
            new Token(TokenKind.STRING_LITERAL, "\"string\"", null),
            new Token(TokenKind.STRING_LITERAL, "\"\\н\"", null),
            new Token(TokenKind.STRING_LITERAL, "\"\\т\"", null),
            new Token(TokenKind.STRING_LITERAL, "\"\\\"\"", null),
            new Token(TokenKind.STRING_LITERAL, "\"\\\\\"", null)
        };
        try {
            for (int i = 0; i < elements.length; ++i) {
                assertTrue(tokens[i].getImage(), ConstantsParser.parse(tokens[i]).equals(elements[i]));
            }
        } catch (StackElementException e) {
            fail(e.toString());
        } catch (ParseTreeVisitorException e) {
            fail(e.toString());
        }
    }
}
