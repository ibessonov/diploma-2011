package sl.program.commands;

import sl.elements.StackElement;
import sl.program.Command;
import sl.program.ProgramsStack;
import sl.program.Counter;

public class AssignCommand implements Command {

    @Override
    public void execute(ProgramsStack stack, Counter counter) throws Exception {
        StackElement right = stack.pop();
        StackElement left = stack.pop();
        left.assign(right);
        counter.inc();
    }

    @Override
    public String toString() {
        return "присвоить";
    }
}
