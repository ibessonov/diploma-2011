package sl.elements;

import sl.elements.string.StringElement;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class StringElementTest {

    private StringElement sE;
    private String value;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        sE = new StringElement(null);
        value = "abc";
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testAssign() {
        StringElement sElem = new StringElement(value);
        try {
            sE.assign(sElem);
            assertTrue(sE.value().equals(value));
        } catch (StackElementException e) {
            fail(e.toString());
        }
    }

    @Test
    public void testEqualsStackElement() throws StackElementException {

        sE.setValue(value);
        assertTrue(sE.equals(new StringElement(value)));
    }

    @Test
    public void testClone() throws StackElementException {
        sE.setValue(value);
        assertTrue(sE.equals(sE.clone()));
    }
}
