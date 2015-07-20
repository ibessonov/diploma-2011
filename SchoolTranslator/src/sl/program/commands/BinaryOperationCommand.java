package sl.program.commands;

import sl.operations.ExecuteOperation;
import sl.elements.StackElement;
import sl.operations.binary.BinaryOperation;
import sl.program.Command;
import sl.program.ProgramsStack;
import sl.program.Counter;

public class BinaryOperationCommand implements Command {

    private BinaryOperation id;

    public BinaryOperationCommand(BinaryOperation id) {
        this.id = id;
    }

    @Override
    public void execute(ProgramsStack stack, Counter counter) throws Exception {
        StackElement right = stack.pop();
        StackElement left = stack.pop();
        StackElement result = ExecuteOperation.binary(id, left, right);
        stack.push(result);
        counter.inc();
    }

    @Override
    public String toString() {
        return "опреация " + id.toString();
    }
}
