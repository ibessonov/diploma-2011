package sl.program.commands;

import sl.program.Command;
import sl.program.Counter;
import sl.program.ProgramsStack;
import sl.elements.StackElement;

public final class PushCommand implements Command {

    private int address;

    public PushCommand(int address) {
        this.address = address;
    }

    @Override
    public void execute(ProgramsStack stack, Counter counter) throws Exception {
        StackElement elem = stack.element(stack.offset() + address);
        stack.push(elem);
        counter.inc();
    }

    @Override
    public String toString() {
        return "положить на вершину " + address;
    }
}
