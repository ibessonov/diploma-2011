package sl.elements;

import sl.elements.string.StringElement;
import sl.elements.bool.BooleanElement;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import sl.elements.bool.BooleanType;

public class BooleanElementTest {

    private BooleanElement bE;
    private boolean value;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        bE = new BooleanElement(false);
        value = true;
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetType() {
        assertTrue(bE.type().equals(BooleanType.get()));

    }

    @Test
    public void testRead() {
        return;
    }

    @Test
    public void testWrite() {
        return;
    }

    @Test
    public void testAssign() {
        BooleanElement cElem = new BooleanElement(value);
        try {
            bE.assign(cElem);
            assertTrue(cElem.equals(BooleanElement.TRUE));
        } catch (StackElementException e) {
            fail(e.toString());
        }
    }

    @Test
    public void testEqualsStackElement() {
        try {
            assertTrue(BooleanElement.TRUE.equals(BooleanElement.TRUE));
            assertTrue(BooleanElement.FALSE.equals(BooleanElement.FALSE));
            assertFalse(BooleanElement.TRUE.equals(BooleanElement.FALSE));
            assertFalse(BooleanElement.FALSE.equals(BooleanElement.TRUE));
        } catch (StackElementException e) {
            fail(e.toString());
        }
    }

    @Test
    public void testClone() {
        bE = BooleanElement.get(value);
        try {
            assertTrue(bE.equals(bE.clone()));
        } catch (StackElementException e) {
            fail(e.toString());
        }
    }
}
