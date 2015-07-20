package sl.plugins;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс представляет собой фабрику всех доступных функций языка. Реализован как
 * Singleton, чтобы набор функций был общим для всех.
 * @author Полевая Евгения
 */
public final class FunctionsFactory {

    static private FunctionsFactory instance = null;
    private Map<String, Function> map;

    private FunctionsFactory() {
        map = new HashMap<String, Function>();
    }

    /**
     * Добавление функции к фабрике. Если одноименная функция уже существует,
     * то она заменяется на новую
     * @param function Новая функция. Ей имя получается вызовом метода
     * <tt>Function.getName()</tt>
     */
    public synchronized void add(Function function) {
        map.put(function.getName(), function);
    }

    /**
     * Получение функции по имени
     * @param name Имя функции
     * @return Объект <tt>Function</tt>, представляющий функцию.
     * <tt>null</tt>, если функция с таким именем не найдена
     */
    public synchronized Function get(String name) {
        return map.get(name);
    }

    /**
     * Получение единственного экземпляра класса
     * @return Объект FunctionsFactory
     */
    public static FunctionsFactory instance() {
        return (instance == null)
                ? (instance = new FunctionsFactory())
                : (instance);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}
