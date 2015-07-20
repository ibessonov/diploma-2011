package sl.plugins;

import sl.elements.Type;
import sl.program.Program;

/**
 * Интерфейс, предназначенный для описания функций языка.
 * @author Полевая Евгения
 */
public interface Function {

    /**
     * Получить имя функции
     * @return Имя функции
     */
    public String getName();

    /**
     * Получить тип возвращаемого значения
     * @return Тип возвращаемого значения, null если таковое отсутствует
     */
    public Type getReturnType();

    /**
     * Получить количество параметров функции
     * @return Количество параметров
     */
    public int getParamsCount();

    /**
     * Получить параметр функции
     * @param index Номер параметра
     * @return Объект {@link Parameter}, представляющий параметр функции
     */
    public Parameter getParameter(int index);

    /**
     * Получить реализацию функции
     * @return Объект {@link Program}, содержащий реализацию
     */
    public Program getProgram();
}
