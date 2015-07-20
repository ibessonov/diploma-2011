package sl.program;

import java.util.Iterator;
import java.util.Scanner;
import java.util.Stack;

/**
 * Класс, реализующий абстрактное вычислительное устройство, на котором
 * выполняются команды {@link sl.program.Command}
 * @param <E> Тип элементов для вычислительного устройства
 * @author Полевая Евгения
 * @version 2.0
 */
public abstract class AbstractStackUnit<E> {

    private Scanner scanner;
    private Appendable writer;
    private Stack<E> elements = new Stack<E>();
    private Stack<Integer> base = new Stack<Integer>();
    private Stack<Integer> offset = new Stack<Integer>();
    private Stack<Integer> ret = new Stack<Integer>();

    /**
     * Конструктор
     * @param reader Поток ввода данных во время исолнения
     * @param writer Поток вывода данных во время исполнения
     */
    public AbstractStackUnit(Readable reader, Appendable writer) {
        this.scanner = new Scanner(reader);
        this.writer = writer;
        this.base.add(0);
        this.offset.add(0);
    }

    /**
     * @return Сканер потока ввода, переданного в конструкторе
     */
    public Scanner scanner() {
        return scanner;
    }

    /**
     * @return Поток вывода, переданный в конструкторе
     */
    public Appendable writer() {
        return writer;
    }

    /**
     * Вызов функции на устройстве
     * @param address Адрес начала функции в коде программы
     * @param index Адрес команды вызова функции
     */
    public void call(int address, int index) {
        base.push(address);
        ret.push(index);
        offset.push(size());
    }

    /**
     * Возврат из функции на устройстве
     * @return Номер следующей команды для исполнения
     */
    public int ret() {
        base.pop();
        offset.pop();
        return ret.pop() + 1;
    }

    /**
     * @return Номер первой команды текущей функции в программе
     */
    public int base() {
        return base.peek();
    }

    /**
     * @return Смещение в стеке начала блока локальных переменных текущей
     * вызванной функции
     */
    public int offset() {
        return offset.peek();
    }

    /**
     * @param index Номер функции
     * @return Смещение в стеке начала блока локальных переменных текущей
     * вызванной функции с номером index в стеке вызовов
     */
    public int offset(int index) {
        return offset.get(index + 1);
    }

    /**
     * Добавление элемента на вершину программного стека
     * @param elem Элемент, который нужно добавить
     */
    public void push(E elem) {
        elements.push(elem);
    }

    /**
     * Извлечь вершину программного стека
     * @return Значение удаленной вершины
     */
    public E pop() {
        return elements.pop();
    }

    /**
     * @return Значение вершины программного стека
     */
    public E top() {
        return elements.peek();
    }

    /**
     * @param index
     * @return Элемент программного стека с номером index
     */
    public E element(int index) {
        return elements.get(index);
    }

    /**
     * @return Итератор на стек вызовов, содержит точки входа в функции
     */
    public Iterator<Integer> callStack() {
        return base.listIterator(1);
    }

    /**
     * Сброс устройства в исходное инициализированное состоянои
     */
    public void clear() {
        this.elements.clear();
        this.base.clear();
        this.offset.clear();
        this.base.add(0);
        this.offset.add(0);
    }

    /**
     * @return Количество элементов в программном стеке
     */
    public int size() {
        return elements.size();
    }
}
