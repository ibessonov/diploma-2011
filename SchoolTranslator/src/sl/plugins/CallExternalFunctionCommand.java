package sl.plugins;

import sl.program.Command;
import sl.program.Program;
import sl.program.ProgramsStack;
import sl.program.Counter;

/**
 * Команда вызова внешней функции
 * @author Полевая Евгения
 */
public class CallExternalFunctionCommand implements Command {

    private String name;

    /**
     * Конструктор
     * @param name Имя вызываемой функции. Она обязана содержаться в фабрике
     * {@link FunctionsFactory}
     */
    public CallExternalFunctionCommand(String name) {
        this.name = name;
    }

    @Override
    public void execute(ProgramsStack stack, Counter counter)
            throws Exception {
        Function function = FunctionsFactory.instance().get(name);
        Program program = function.getProgram();
        Counter c = new Counter(program.size());
        while(!c.end()) {
            program.getCommand(c.get()).execute(stack, c);
        }
        counter.inc();
    }

    @Override
    public String toString() {
        return "Вызов внешней функции: \"" + name + "\"";
    }
}
