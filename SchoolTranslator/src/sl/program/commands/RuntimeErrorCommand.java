package sl.program.commands;

import sl.program.Command;
import sl.program.ProgramsStack;
import sl.program.Counter;

public class RuntimeErrorCommand implements Command {

    private Exception t;

    public RuntimeErrorCommand(Exception ex) {
        this.t = ex;
    }

    @Override
    public void execute(ProgramsStack stack, Counter counter) throws Exception {
        throw t;
    }
}
