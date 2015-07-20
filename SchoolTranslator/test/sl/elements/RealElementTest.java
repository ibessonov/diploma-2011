package sl.elements;

import sl.elements.string.StringElement;
import sl.elements.real.RealElement;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sl.elements.real.RealType;

public class RealElementTest {

    private RealElement rE;
    private double number;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        rE = new RealElement(0);
        number = 5.;
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testAssign() {
        RealElement rEl = new RealElement(number);
        try {
            rE.assign(rEl);
            assertTrue(rE.value() == number);
        } catch (StackElementException e) {
            fail(e.toString());
        }
    }

    @Test
    public void testEqualsStackElement() {
        rE.setValue(number);
        try {
            rE.equals(new RealElement(number));
        } catch (StackElementException e) {
            fail(e.toString());
        }
    }

    @Test
    public void testClone() {
        rE.setValue(number);
        try {
            assertTrue(rE.equals(rE.clone()));
        } catch (StackElementException e) {
            fail(e.toString());
        }
    }
}
