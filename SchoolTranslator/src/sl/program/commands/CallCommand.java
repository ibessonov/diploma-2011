package sl.program.commands;

import sl.program.Command;
import sl.program.Counter;
import sl.program.ProgramsStack;

public final class CallCommand implements Command {

    private int index;

    public CallCommand(int index) {
        this.index = index;
    }

    public void setAddress(int index) {
        this.index = index;
    }

    @Override
    public void execute(ProgramsStack stack, Counter counter) throws Exception {
        stack.call(index, counter.get());
        counter.set(index);
    }

    @Override
    public String toString() {
        return "вызов " + index;
    }
}
