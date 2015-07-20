package sl.program;

/**
 * Класс используется для исполнения программ в промежуточном представлении
 * @author Полевая Евгения
 * */
public final class Executor implements Runnable {

    private Program program;
    private ProgramsStack stack;
    private Counter counter;
    private Stopper stopper;

    /**
     * Конструктор
     * @param program Программа, для исполнения которой создается исполнитель
     * @param reader Поток ввода для выполнения программы
     * @param writer Поток вывода для выполнения программы
     * */
    public Executor(Program program,
            Readable reader, Appendable writer) {
        this.program = program;
        stack = new ProgramsStack(reader, writer);
        counter = new Counter(program.size());
        stopper = new Stopper();
    }

    /**
     * Выполнение программы
     * @throws Exception Возникает в случае неудачного завершения исполнения
     * одной из команд программы
     * */
    @Override
    public void run() throws Exception {
        counter.set(0);
        while (!counter.end() &&  !stopper.isStopped()) {
            currentCommand().execute(stack, counter);
        }
        stack.clear();
    }

    @Override
    public void stop() {
        stopper.stop();
    }

    private Command currentCommand() {
        return program.getCommand(counter.get());
    }

    private static class Stopper {
        private boolean stop = false;

        public synchronized void stop() {
            stop = true;
        }

        public synchronized boolean isStopped() {
            return stop;
        }
    }
}
