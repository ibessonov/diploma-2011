/**
 * 
 */
package sl.program;

import sl.program.Counter;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Ivan
 *
 */
public class CounterTest {
    
    private Counter counter;
    private int size;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
	size = 10;
	counter = new Counter(size);
    }

    /**
     * Test method for {@link program.Counter#get()} and {@link program.Counter#set(int).
     */
    @Test
    public void testCurrent() {
	int current = 5;
	counter.set(current);
	assertTrue(current == counter.get());
    }

    /**
     * Test method for {@link program.Counter#inc()}.
     */
    @Test
    public void testInc() {
	int current = 5;
	counter.set(current);
	counter.inc();
	assertTrue(counter.get() == current + 1);
    }

    /**
     * Test method for {@link program.Counter#end()}.
     */
    @Test
    public void testEnd() {
	int current = size - 1;
	counter.set(current);
	assertFalse(counter.end());
	counter.inc();
	assertTrue(counter.end());
    }

}
