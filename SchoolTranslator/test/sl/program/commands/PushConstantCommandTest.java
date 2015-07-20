package sl.program.commands;

import static org.junit.Assert.*;

import org.junit.Test;

import sl.program.Command;
import sl.program.Counter;
import sl.program.ProgramsStack;
import sl.elements.StackElement;
import sl.elements.integer.IntegerElement;

public class PushConstantCommandTest {

    @Test
    public void testExecute() {
	ProgramsStack stack = new ProgramsStack(null, null);
	StackElement elem = new IntegerElement(15);
	Counter counter = new Counter(2);
	Command command = new PushConstantCommand(elem);
	try {
	    command.execute(stack, counter);
	} catch (Exception e) {
	    fail(e.toString());
	}
	assertTrue(stack.size() == 1);
	assertTrue(stack.top() == elem);
	assertTrue(counter.get() == 1);
    }

}
