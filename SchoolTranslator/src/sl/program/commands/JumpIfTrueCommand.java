package sl.program.commands;

import sl.program.Command;
import sl.program.Counter;
import sl.program.ProgramsStack;
import sl.elements.bool.BooleanElement;

public final class JumpIfTrueCommand implements Command {

    private int index;

    public JumpIfTrueCommand(int index) {
        this.index = index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public void execute(ProgramsStack stack, Counter counter) throws Exception {
        if (BooleanElement.TRUE.equals(stack.pop())) {
            counter.set(stack.base() + index);
        } else {
            counter.inc();
        }
    }

    @Override
    public String toString() {
        return "переход если да " + index;
    }
}
