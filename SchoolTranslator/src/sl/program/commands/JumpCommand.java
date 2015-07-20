package sl.program.commands;

import sl.program.Command;
import sl.program.Counter;
import sl.program.ProgramsStack;

public final class JumpCommand implements Command {

    private int index;

    public JumpCommand(int index) {
        this.index = index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public void execute(ProgramsStack stack, Counter counter) throws Exception {
        counter.set(stack.base() + index);
    }

    @Override
    public String toString() {
        return "переход " + index;
    }
}
