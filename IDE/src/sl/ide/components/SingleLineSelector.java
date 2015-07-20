package sl.ide.components;

import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

public class SingleLineSelector {

    private final JTextArea textArea;
    private Object highlight;
    private Highlighter.HighlightPainter painter;

    public SingleLineSelector(JTextArea textArea) {
        this.textArea = textArea;
        textArea.setHighlighter(new DefaultHighlighter());
        highlight = null;
        painter = new DefaultHighlighter.DefaultHighlightPainter(new Color(0xC0C0FF));
    }

    public void selectLine(int line) {
        unselectLine();
        try {
            highlight = textArea.getHighlighter().
                    addHighlight(textArea.getLineStartOffset(line),
                    textArea.getLineEndOffset(line), painter);
        } catch (BadLocationException ex) {
        }
    }

    public void unselectLine() {
        synchronized (textArea) {
            if (highlight != null) {
                textArea.getHighlighter().removeHighlight(highlight);
            }
            textArea.repaint();
        }
    }
}
