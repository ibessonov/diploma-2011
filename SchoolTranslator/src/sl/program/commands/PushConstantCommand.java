package sl.program.commands;

import sl.program.Command;
import sl.program.Counter;
import sl.program.ProgramsStack;
import sl.elements.StackElement;

public final class PushConstantCommand implements Command {

    private StackElement elem;

    public PushConstantCommand(StackElement elem) {
        this.elem = elem;
    }

    @Override
    public void execute(ProgramsStack stack, Counter counter) throws Exception {
        stack.push(elem);
        counter.inc();
    }

    @Override
    public String toString() {
        return "положить константу на вершину " + elem;
    }
}
