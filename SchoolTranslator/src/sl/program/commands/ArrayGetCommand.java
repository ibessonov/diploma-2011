package sl.program.commands;

import sl.elements.array.ArrayElement;
import sl.elements.integer.IntegerElement;
import sl.elements.integer.IntegerType;
import sl.program.Command;
import sl.program.ProgramsStack;
import sl.program.Counter;

public class ArrayGetCommand implements Command {

    @Override
    public void execute(ProgramsStack stack, Counter counter) throws Exception {
        IntegerElement offset = (IntegerElement) IntegerType.get().convert(stack.pop());
        ArrayElement array = (ArrayElement) stack.pop();
        stack.push(array.get(offset.value()));
        counter.inc();
    }

    @Override
    public String toString() {
        return "взять элемент массива";
    }
}
