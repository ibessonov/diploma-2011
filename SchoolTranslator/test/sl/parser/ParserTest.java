/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sl.parser;

import sl.parser.nodes.Node;
import java.io.ByteArrayInputStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ivan
 */
public class ParserTest {

    private Parser parser;

    public ParserTest() {
        parser = new Parser(System.in);
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of parse method, of class Parser.
     */
    @Test
    public void testSimpleParse() {
        testParse("алг в;нач возврат; кон");
    }

    private void testParse(String text) {
        parser.reInit(new ByteArrayInputStream(text.getBytes()));
        try {
            parser.parse(new ParseTreeBuilder() {

                @Override
                public void openNode(Node node) throws ParseException {
                }

                @Override
                public void closeNode() {
                }

                @Override
                public void clear() {
                }

                @Override
                public ParseTree result() {
                    return null;
                }
            });
        } catch (Exception ex) {
            fail();
        }
    }
}
