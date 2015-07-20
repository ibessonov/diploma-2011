package sl.program.commands;

import sl.program.Command;
import sl.program.Counter;
import sl.program.ProgramsStack;
import sl.elements.StackElement;

public final class SwapCommand implements Command {

    @Override
    public void execute(ProgramsStack stack, Counter counter) throws Exception {
        StackElement first = stack.pop();
        StackElement second = stack.pop();
        stack.push(first);
        stack.push(second);
        counter.inc();
    }

    @Override
    public String toString() {
        return "поменять местами значения на вершине";
    }
}
