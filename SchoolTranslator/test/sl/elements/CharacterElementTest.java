package sl.elements;

import sl.elements.character.CharacterElement;
import sl.elements.integer.IntegerElement;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sl.elements.StackElement;
import sl.elements.StackElementException;
import sl.elements.character.CharacterType;

public class CharacterElementTest {

    private CharacterElement cE;
    private char value;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        cE = new CharacterElement('\0');
        value = 'a';
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testAssign() {
        CharacterElement cElem = new CharacterElement(value);
        try {
            cE.assign(cElem);
            assertTrue(cElem.value() == value);
        } catch (StackElementException e) {
            fail(e.toString());
        }
    }

    @Test
    public void testEqualsStackElement() {

        cE.setValue(value);
        try {
            cE.equals(new CharacterElement(value));
        } catch (StackElementException e) {
            fail(e.toString());
        }
    }

    @Test
    public void testClone() {
        cE.setValue(value);
        try {
            assertTrue(cE.equals(cE.clone()));
        } catch (StackElementException e) {
            fail(e.toString());
        }
    }
}
