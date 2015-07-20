package sl.program.commands;

import sl.program.Command;
import sl.program.Counter;
import sl.program.ProgramsStack;

public final class WriteCommand implements Command {

    @Override
    public void execute(ProgramsStack stack, Counter counter) throws Exception {
        stack.pop().write(stack.writer());
        counter.inc();
    }

    @Override
    public String toString() {
        return "вывести";
    }
}
