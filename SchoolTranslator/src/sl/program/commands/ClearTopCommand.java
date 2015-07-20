package sl.program.commands;

import sl.program.Command;
import sl.program.Counter;
import sl.program.ProgramsStack;

public final class ClearTopCommand implements Command {

    @Override
    public void execute(ProgramsStack stack, Counter counter) throws Exception {
        stack.pop();
        counter.inc();
    }

    @Override
    public String toString() {
        return "очистить вершину";
    }
}
