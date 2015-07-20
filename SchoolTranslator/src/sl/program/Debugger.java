package sl.program;

import sl.program.commands.CallCommand;
import sl.program.commands.InitializeCommand;
import sl.program.commands.ReturnCommand;
import static sl.program.Debugger.Mode.*;

/**
 *
 * @author Полевая Евгения
 */
public final class Debugger implements Runnable {

    private Program program;
    private DebugInformation debugInfo;
    private BreakpointsSet breakpointsSet;
    private Counter counter;
    private ProgramsStack stack;
    private Mode mode;

    public static enum Mode {

        STEP_INTO,
        STEP_OVER,
        NEXT_BREAKPOINT,
        STOP
    }

    public Debugger(Program program, Readable reader, Appendable writer,
            DebugInformation debugInfo, BreakpointsSet breakpointsSet) {
        this.program = program;
        this.debugInfo = debugInfo;
        this.breakpointsSet = breakpointsSet;
        this.counter = new Counter(program.size());
        this.mode = NEXT_BREAKPOINT;
        this.stack = new ProgramsStack(reader, writer);
    }

    public synchronized Mode getMode() {
        return mode;
    }

    public synchronized void setMode(Mode mode) {
        this.mode = mode;
    }

    @Override
    public synchronized void run() throws Exception {
        switch (mode) {
            case STEP_INTO:
                stepInto();
                break;
            case NEXT_BREAKPOINT:
                nextBreakpoint();
                break;
            case STEP_OVER:
                stepOver();
                break;
            case STOP:
                stack.clear();
                counter.set(0);
                mode = NEXT_BREAKPOINT;
                break;
        }
    }

    @Override
    public void stop() {
        mode = STOP;
    }

    public synchronized int getCurrentRow() {
        return currentCommandRow();
    }

    public synchronized ProgramsStack getProgramsStack() {
        return stack;
    }

    public synchronized boolean isFinished() {
        return counter.end() || mode == STOP;
    }

    private void stepInto() throws Exception {
        int currentRow = currentCommandRow();
        boolean callReturn = false;
        do {
            callReturn = currentCommand() instanceof CallCommand ||
                    currentCommand() instanceof ReturnCommand;
            currentCommand().execute(stack, counter);
            if (callReturn) {
                while (currentCommand() instanceof InitializeCommand) {
                    currentCommand().execute(stack, counter);
                }
            }
        } while (!isFinished() && !callReturn && currentCommandRow() == currentRow);
        skipLastLine();
    }

    private void nextBreakpoint() throws Exception {
        do {
            stepInto();
        } while (!isFinished() && !isBreakpont());
    }

    private void stepOver() throws Exception {
        int depth = 0;
        int currentRow = currentCommandRow();
        do {
            Command currentCommand = currentCommand();
            if (currentCommand instanceof CallCommand) {
                ++depth;
            } else if (currentCommand instanceof ReturnCommand) {
                --depth;
            }
            currentCommand.execute(stack, counter);
        } while (!isFinished() && depth >= 0
                && (depth != 0 || currentCommandRow() == currentRow)
                && (depth == 0 || !isBreakpont()));
        skipLastLine();
    }
    
    private void skipLastLine() throws Exception {
        while(!isFinished() && currentCommandRow() == DebugInformation.SKIP_LINE) {
            currentCommand().execute(stack, counter);
        }
    }

    private Command currentCommand() {
        return program.getCommand(counter.get());
    }

    private int currentCommandRow() {
        return debugInfo.getCommandRow(counter.get());
    }

    private boolean isBreakpont() {
        return breakpointsSet.has(currentCommandRow());
    }
}
