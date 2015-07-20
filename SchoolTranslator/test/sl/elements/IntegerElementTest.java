package sl.elements;

import sl.elements.real.RealElement;
import sl.elements.integer.IntegerElement;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sl.elements.StackElement;
import sl.elements.StackElementException;
import sl.elements.integer.IntegerType;

public class IntegerElementTest {

    private IntegerElement iE;
    private int number;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        iE = new IntegerElement(0);
        number = 5;
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testAssign() {
        IntegerElement iEl = new IntegerElement(number);
        try {
            iE.assign(iEl);
            assertTrue(iE.value() == number);
        } catch (StackElementException e) {
            fail(e.toString());
        }
    }

    @Test
    public void testEqualsStackElement() {

        iE.setValue(number);
        try {
            iE.equals(new IntegerElement(number));
        } catch (StackElementException e) {
            fail(e.toString());
        }
    }

    @Test
    public void testClone() {
        iE.setValue(number);
        try {
            assertTrue(iE.equals(iE.clone()));
        } catch (StackElementException e) {
            fail(e.toString());
        }
    }
}
