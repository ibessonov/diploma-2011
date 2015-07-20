package sl.ide.tasks;

import org.jdesktop.application.Application;
import org.jdesktop.application.Task;

public abstract class ExecuteTask extends Task<Void, Void> {

    public ExecuteTask(Application application) {
        super(application);
    }

    public abstract void stopExecution();
}
