package sl.program;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс предсталяет собой промежуточное представление программы, а именно
 * последовательный набор объектов {@link Command}
 * @author Полевая Евгения
 * @version 2.0
 * */
public final class Program {

    private List<Command> commands;

    /**
     * Конструктор
     */
    public Program() {
        commands = new ArrayList<Command>();
    }

    /**
     * Добавление команды к программе
     * @param command Команда, добавляемая к программе
     * */
    public void append(Command command) {
        commands.add(command);
    }

    /**
     * Добавление команд к программе
     * @param program Объект Program, команды которого добавляются к программе
     */
    public void append(Program program) {
        commands.addAll(program.commands);
    }

    /**
     * Получение команды по индексу
     * @param index Номер команды
     * @return Команда
     */
    public Command getCommand(int index) {
        return commands.get(index);
    }

    /**
     * @return Размер программы в командах
     */
    public int size() {
        return commands.size();
    }

    /**
     * Очистка программы
     */
    public void clear() {
        commands.clear();
    }

    @Override @Deprecated
    public String toString() {
        StringBuilder builder = new StringBuilder();
        int i = 0;
        for (Command c : commands) {
            builder.append(i++);
            builder.append(":\t");
            builder.append(c.toString());
            builder.append('\n');
        }
        return builder.toString();
    }
}
