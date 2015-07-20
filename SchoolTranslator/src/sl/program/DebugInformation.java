package sl.program;

import java.util.Set;

/**
 *
 * @author Полевая Евгения
 */
public interface DebugInformation {

    public static final int SKIP_LINE = -1;

    /**
     * Получить номер команды
     * @param index Номер строки в программе
     * @return Номер команды в промежуточном представлении программы
     */
    public int getCommandRow(int index);

    /**
     * Получить имя функции
     * @param index Номер команды
     * @return Имя функции
     */
    public String getFunctionName(int index);

    /**
     * Получить набор имен переменных по имени функции
     * @param function Имя функции
     * @return Набор имен переменных
     */
    public Set<String> getVariables(String function);

    /**
     * Получить смещение в стеке переменной относительно начала блока
     * локальных переменных в функции
     * @param function имя функции
     * @param variable имя переменной
     * @return Смещение в стеке
     */
    public Integer getVariableOffset(String function, String variable);
}
