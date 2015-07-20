package sl.parser;

import sl.parser.StreamTokenizer;
import sl.parser.Token;
import java.io.ByteArrayInputStream;
import org.junit.Test;
import static org.junit.Assert.*;
import static sl.parser.TokenKind.*;

public class StreamTokenizerTest {

    private StreamTokenizer analyzer;

    public StreamTokenizerTest() {
        analyzer = new StreamTokenizer(System.in);
    }

    /**
     * Test of nextToken method, of class StreamTokenizer.
     */
    @Test
    public void testNumericalTokens() {
        testToken("0", INTEGER_LITERAL);
        testToken("123", INTEGER_LITERAL);
        testToken("1.0", FLOATING_POINT_LITERAL);
        testToken("1.", FLOATING_POINT_LITERAL);
        testToken("1e1", FLOATING_POINT_LITERAL);
        testToken("1e+1", FLOATING_POINT_LITERAL);
        testToken("1e-1", FLOATING_POINT_LITERAL);
        testToken("1.0e1", FLOATING_POINT_LITERAL);
        testToken("1.e1", FLOATING_POINT_LITERAL);
        testToken("\"string\"", STRING_LITERAL);
        testToken("'c'", CHARACTER_LITERAL);
        testToken("(", OPARENTHESIS);
        testToken(":=", ASSIGN);
    }

    private void testToken(String image, TokenKind kind) {
        try {
            analyzer.reInit(new ByteArrayInputStream(image.getBytes()));
            Token token = analyzer.nextToken();
            assertEquals(token.getImage(), image);
            assertEquals(token.getKind(), kind);
        } catch (Exception ex) {
            fail(ex.toString());
        }
    }
}
