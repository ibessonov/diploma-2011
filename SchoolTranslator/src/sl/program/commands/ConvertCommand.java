package sl.program.commands;

import sl.elements.StackElement;
import sl.elements.Type;
import sl.program.Command;
import sl.program.ProgramsStack;
import sl.program.Counter;

public class ConvertCommand implements Command {

    private Type type;

    public ConvertCommand(Type type) {
        this.type = type;
    }

    @Override
    public void execute(ProgramsStack stack, Counter counter) throws Exception {
        StackElement elem = stack.pop();
        stack.push(type.convert(elem));
        counter.inc();
    }

    @Override
    public String toString() {
        return "преобразовать к типу " + type.toString();
    }
}
