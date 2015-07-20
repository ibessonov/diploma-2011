package sl.ide.components;

import javax.swing.JTextArea;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import sl.ide.io.TextAreaReader;

/**
 * Класс, реализующий поле вывода аналогично консоли.
 * @author Полевая Евгения
 */
public class OutputTextArea extends JTextArea {

    private Readable reader = null;
    private int fixedCount = 0;
    private String lastInput = "";

    /**
     * Конструктор
     */
    public OutputTextArea() {
        super();
        super.setEditable(false);
    }

    /**
     * Метод переопределен с пустым телом, т.к. контроль за возможностью
     * редактирования текста производится самим классом
     * @param b игнорируется
     */
    @Override
    public void setEditable(boolean b) {
    }

    /**
     * Установить связанный с объектом поток ввода.
     * Использование на примере {@link TextAreaReader}:<tt>
     * <br>OutputTextArea area = new OutputTextArea();
     * <br>TextAreaReader reader = new TextAreaReader(area);
     * <br>area.setDataReader(reader);
     * </tt>
     * @param reader Поток ввода
     */
    public void setDataReader(Readable reader) {
        this.reader = reader;
    }

    /**
     * Перевести текстовую область в режим возможности ввода. Окончанием
     * считается ввод символа '\n' в самом конце текстовой области. После этого
     * ввод запрещается
     */
    public void setReadMode() {
        super.setEditable(true);
        fixedCount = getText().length();
    }

    /**
     * @return Последняя введенная с клавиатуры строка
     */
    public String getLastInput() {
        return lastInput;
    }

    @Override
    protected Document createDefaultModel() {
        return new PlainDocument() {

            @Override
            public void insertString(int offs, String str, AttributeSet a)
                    throws BadLocationException {
                if (changeAccessable(offs)) {
                    if (OutputTextArea.super.isEditable()
                            && (offs == OutputTextArea.this.getText().length())
                            && "\n".equals(str)) {
                        setWriteMode();
                    }
                    super.insertString(offs, str, a);
                }
            }

            @Override
            public void remove(int offs, int len) throws BadLocationException {
                if (changeAccessable(offs)) {
                    super.remove(offs, len);
                }
            }

            @Override
            public void replace(int offs, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                if (changeAccessable(offs)) {
                    super.replace(offs, length, text, attrs);
                }
            }
        };
    }

    private boolean changeAccessable(int offs) {
        return !super.isEditable() || (offs >= fixedCount);
    }

    private void setWriteMode() {
        super.setEditable(false);
        int length = getText().length();
        synchronized (reader) {
            try {
                lastInput = getText(fixedCount, length - fixedCount + 1);
            } catch (BadLocationException ex) {
            }
            reader.notify();
        }
    }
}
