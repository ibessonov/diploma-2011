package sl.program.commands;

import sl.elements.Type;
import sl.program.Command;
import sl.program.ProgramsStack;
import sl.program.Counter;

public class InitializeCommand implements Command {

    private Type type;

    public InitializeCommand(Type type) {
        this.type = type;
    }

    @Override
    public void execute(ProgramsStack stack, Counter counter) throws Exception {
        stack.push(type.instance());
        counter.inc();
    }

    @Override
    public String toString() {
        return "создать переменную типа " + type.toString();
    }
}
