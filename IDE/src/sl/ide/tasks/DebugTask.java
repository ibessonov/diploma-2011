package sl.ide.tasks;

import org.jdesktop.application.Application;
import sl.program.BreakpointsSet;
import sl.program.DebugInformation;
import sl.program.Debugger;
import sl.program.ProgramsStack;
import sl.translator.SLTranslator;
import sl.plugins.SLTranslatorUtils;

/**
 *
 * @author
 */
public class DebugTask extends ExecuteTask {

    private String text;
    private Readable reader;
    private Appendable writer;
    private Debugger debugger = null;
    private BreakpointsSet brSet;
    private DebugInformation debugInfo = null;

    public DebugTask(Application app, String text,
            Readable reader, Appendable writer, BreakpointsSet brSet) {
        super(app);
        this.text = text;
        this.reader = reader;
        this.writer = writer;
        this.brSet = brSet;
    }

    @Override
    protected Void doInBackground() throws Exception {
        SLTranslator translator = SLTranslatorUtils.stringToSLTranslator(text);
        debugger = new Debugger(translator.getProgram(), reader, writer,
                debugInfo = translator.getDebugInformation(), brSet);
        while (!isCancelled() && !debugger.isFinished()) {
            debugger.run();
            if (debugger.isFinished()) {
                return null;
            }
            synchronized (this) {
                beforeWait();
                wait();
            }
        }
        return null;
    }

    public synchronized void setMode(Debugger.Mode mode) {
        debugger.setMode(mode);
    }

    protected synchronized int getCurrentRow() {
        return debugger.getCurrentRow();
    }

    protected synchronized DebugInformation getDebugInformation() {
        return debugInfo;
    }

    protected synchronized ProgramsStack getProgramsStack() {
        return debugger.getProgramsStack();
    }

    protected void beforeWait() {
    }

    @Override
    public void stopExecution() {
        debugger.stop();
    }
}
