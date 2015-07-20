package sl.program.commands;

import sl.program.Command;
import sl.program.ProgramsStack;
import sl.program.Counter;

public class ReplaceTopToCloneCommand implements Command {

    @Override
    public void execute(ProgramsStack stack, Counter counter) throws Exception {
        stack.push(stack.pop().clone());
        counter.inc();
    }

    @Override
    public String toString() {
        return "заменить вершину на копию";
    }
}
