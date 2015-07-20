package sl.program.commands;

import sl.program.Command;
import sl.program.Counter;
import sl.program.ProgramsStack;
import sl.elements.StackElement;

public final class CopyCommand implements Command {

    private int address;

    public CopyCommand(int address) {
        this.address = address;
    }

    @Override
    public void execute(ProgramsStack stack, Counter counter) throws Exception {
        StackElement elem = stack.element(stack.offset() + address);
        stack.push(elem.clone());
        counter.inc();
    }

    @Override
    public String toString() {
        return "копировать " + address;
    }
}
