package sl.program.commands;

import sl.operations.ExecuteOperation;
import sl.elements.StackElement;
import sl.operations.unary.UnaryOperation;
import sl.program.Command;
import sl.program.ProgramsStack;
import sl.program.Counter;

public class UnaryOperationCommand implements Command {

    private UnaryOperation id;

    public UnaryOperationCommand(UnaryOperation id) {
        this.id = id;
    }

    @Override
    public void execute(ProgramsStack stack, Counter counter) throws Exception {
        StackElement elem = stack.pop();
        StackElement result = ExecuteOperation.unary(id, elem);
        stack.push(result);
        counter.inc();
    }

    @Override
    public String toString() {
        return "операция " + id.toString();
    }
}
