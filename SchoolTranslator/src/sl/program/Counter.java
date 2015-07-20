package sl.program;

/**
 * Класс, представляющий собой счетчик команд для вычислительного устройства
 * @author Полевая Евгения
 * @version 2.0
 */
public final class Counter {

    private int position;
    private final int size;

    /**
     * Конструктор
     * @param size Размер программы, для которой создается счетчик
     */
    public Counter(int size) {
        this.size = size;
        this.position = 0;
    }

    /**
     * @return Текущее положение счетчика
     */
    public int get() {
        return position;
    }

    /**
     * Установить счетчик в новую позицию
     * @param position Новая позиция счетчика
     */
    public void set(int position) {
        this.position = position;
    }

    /**
     * Увеличить позицию счетчика на 1
     * @return Новая позиция счетчика
     */
    public int inc() {
        return ++position;
    }

    /**
     * Проверить выход счетчика за границы программы
     * @return true, если позиция лежит в промежутке [0, size - 1]
     */
    public boolean end() {
        return (position >= size || position < 0);
    }
}
