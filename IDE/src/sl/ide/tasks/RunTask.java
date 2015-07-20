package sl.ide.tasks;

import org.jdesktop.application.Application;
import sl.program.Executor;
import sl.program.Program;
import sl.plugins.SLTranslatorUtils;

/**
 *
 * @author Полевая Евгения
 */
public class RunTask extends ExecuteTask {

    private String text;
    private Readable reader;
    private Appendable writer;
    private Executor executor = null;

    public RunTask(Application app, String text,
            Readable reader, Appendable writer) {
        super(app);
        this.text = text;
        this.reader = reader;
        this.writer = writer;
    }

    @Override
    protected Void doInBackground() throws Exception {
        Program program = SLTranslatorUtils.stringToProgram(text);
        executor = new Executor(program, reader, writer);
        if (!isCancelled()) {
            executor.run();
        }
        return null;
    }

    @Override
    public synchronized void stopExecution() {
        if (executor != null) {
            executor.stop();
        } else {
            cancel(false);
        }
    }
}
