package sl.program.commands;

import sl.program.Command;
import sl.program.Counter;
import sl.program.ProgramsStack;
import sl.elements.StackElement;

public final class ReturnCommand implements Command {

    private int paramsSize;
    private boolean hasResult;

    public ReturnCommand(int paramsSize, boolean hasResult) {
        this.paramsSize = paramsSize;
        this.hasResult = hasResult;
    }

    @Override
    public void execute(ProgramsStack stack, Counter counter) throws Exception {
        StackElement result = hasResult ? stack.pop() : null;
        int localsCount = stack.size() - stack.offset();
        for (int i = 0; i < localsCount; ++i) {
            stack.pop();
        }
        int index = stack.ret();
        for (int i = 0; i < paramsSize; ++i) {
            stack.pop();
        }
        if (hasResult) {
            stack.push(result);
        }
        counter.set(index);
    }

    @Override
    public String toString() {
        return "возврат с " + paramsSize + " переметрами"
                + (hasResult ? " и результатом" : "");
    }
}
