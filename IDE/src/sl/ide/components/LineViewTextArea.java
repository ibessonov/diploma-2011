package sl.ide.components;

import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

/**
 * Класс, реализующий нумерацию строк для объекта JTextArea внутри JScrollPane.
 * Пример использования:<pre><tt>
 * JTextArea textArea = new JTextArea();
 * JScrollPane scrollPane = new JScrollPane(textArea);
 * LineViewTextArea lineView = new LineViewTextArea(textArea);
 * scrollPane.setRowHeaderView(lineView);</tt></pre>
 * @see JScrollPane#setRowHeaderView(java.awt.Component)
 * @author Полевая Евгения
 */
public class LineViewTextArea extends JTextArea {

    public static final String eol = System.getProperty("line.separator", "\n");
    private JTextArea textArea;

    /**
     * Конструктор
     * @param textArea Объект, для которого будут отображаться номера строк
     */
    public LineViewTextArea(JTextArea textArea) {
        super();
        this.textArea = textArea;
        init();
    }

    /**
     * @return Объект, для которого отображаются номера строк
     */
    public JTextArea getNumerableTextArea() {
        return textArea;
    }

    /**
     * Синхронный вариант для вызова
     * {@code getNumerableTextArea().getDocument().addDocumentListener(listener);}
     * <br>Наблюдатель получит событие гарантированно после обновления компонента
     * с номерами строк
     * @param listener Объект для прослушивания событий изменений основного документа
     */
    public void addDocumentListener(DocumentListener listener) {
        this.listenerList.add(DocumentListener.class, listener);
    }

    /**
     * Убрать наблюдателя для событий основного документа
     * @param listener Наблюдатель, которого нужно убрать
     * @see LineViewTextArea#addDocumentListener(javax.swing.event.DocumentListener)
     */
    public void removeDocumentListener(DocumentListener listener) {
        this.listenerList.remove(DocumentListener.class, listener);
    }

    private void init() {
        this.setBackground(new Color(0x00E0E0E0)); //светло серый
        this.setFont(textArea.getFont());
        this.setEditable(false);
        this.setFocusable(false);
        updateLineView();
        textArea.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                updateLineView();
                for (DocumentListener listener :
                        listenerList.getListeners(DocumentListener.class)) {
                    listener.insertUpdate(e);
                }
            }

            public void removeUpdate(DocumentEvent e) {
                updateLineView();
                for (DocumentListener listener :
                        listenerList.getListeners(DocumentListener.class)) {
                    listener.removeUpdate(e);
                }
            }

            public void changedUpdate(DocumentEvent e) {
                updateLineView();
                for (DocumentListener listener :
                        listenerList.getListeners(DocumentListener.class)) {
                    listener.changedUpdate(e);
                }
            }
        });
    }

    private void updateLineView() {
        int textLineCount = textArea.getLineCount();
        int viewLineCount = this.getLineCount() - 1;
        if (textLineCount > viewLineCount) {
            StringBuilder builder = new StringBuilder();
            while (textLineCount > viewLineCount) {
                builder.append(String.format("%d %s", ++viewLineCount, eol));
            }
            this.append(builder.toString());
        } else if (textLineCount < viewLineCount) {
            try {
                int offset = this.getLineStartOffset(textLineCount);
                int length = this.getText().length() - offset;
                getDocument().remove(offset, length);
            } catch (BadLocationException ex) {
            }
        }
    }
}
