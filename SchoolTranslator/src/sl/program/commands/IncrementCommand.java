package sl.program.commands;

import sl.elements.integer.IntegerElement;
import sl.elements.integer.IntegerType;
import sl.program.Command;
import sl.program.ProgramsStack;
import sl.program.Counter;

public class IncrementCommand implements Command {

    @Override
    public void execute(ProgramsStack stack, Counter counter) throws Exception {
        IntegerElement elem = (IntegerElement) IntegerType.get().convert(stack.top());
        elem.setValue(elem.value() + 1);
        counter.inc();
    }

    @Override
    public String toString() {
        return "увеличить на 1";
    }
}
