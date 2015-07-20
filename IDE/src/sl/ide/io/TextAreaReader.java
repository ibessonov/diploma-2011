package sl.ide.io;

import sl.ide.components.OutputTextArea;
import java.io.IOException;
import java.nio.CharBuffer;

/**
 * Реализация Readable для графического интерфейса
 * @author Полевая Евгения
 */
public class TextAreaReader implements Readable {

    private OutputTextArea textArea;

    /**
     * Конструктор
     * @param textArea текстовой поля для считывания данных
     */
    public TextAreaReader(OutputTextArea textArea) {
        this.textArea = textArea;
    }

    public synchronized int read(CharBuffer cb) throws IOException {
        textArea.setReadMode();
        try {
            wait();
        } catch (InterruptedException ex) {
        }
        String lastInput = textArea.getLastInput();
        cb.append(lastInput);
        return lastInput.length();
    }
}
