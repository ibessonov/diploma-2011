package sl.ide;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import sl.ide.components.LineViewTextArea;
import sl.program.BreakpointsSet;

public class BreakpointsManager implements BreakpointsSet {

    private int lines;
    private LineViewTextArea lineViewTextArea;
    private Highlighter highlighter;
    private Highlighter.HighlightPainter painter;
    private Map<Integer, Object> map;

    public BreakpointsManager(LineViewTextArea lineView) {
        super();
        this.lines = lineView.getLineCount() - 1;
        this.lineViewTextArea = lineView;
        this.highlighter = new DefaultHighlighter();
        this.painter = new DefaultHighlighter.DefaultHighlightPainter(new Color(0xFF8080));
        this.map = new HashMap<Integer, Object>();
        init();
    }

    public void clear() {
        for (int line : map.keySet()) {
            highlighter.removeHighlight(map.remove(line));
        }
        map.clear();
        this.lines = lineViewTextArea.getLineCount() - 1;
    }

    @Override
    public synchronized boolean has(int line) {
        return map.get(line) != null;
    }

    public synchronized void toggleBreakpoint(int line) {
        if (map.containsKey(line)) {
            highlighter.removeHighlight(map.remove(line));
        } else {
            try {
                map.put(line, highlighter.addHighlight(
                        lineViewTextArea.getLineStartOffset(line),
                        lineViewTextArea.getLineEndOffset(line) - 1
                        - LineViewTextArea.eol.length(),
                        painter));
            } catch (BadLocationException ex) {
            }
        }
    }

    private void init() {
        this.lineViewTextArea.setHighlighter(this.highlighter);
        this.lineViewTextArea.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    try {
                        int line = lineViewTextArea.getLineOfOffset(
                                lineViewTextArea.getSelectionStart());
                        if (line + 1 != lineViewTextArea.getLineCount()) {
                            toggleBreakpoint(line);
                        }
                    } catch (BadLocationException ex) {
                    }
                }
            }
        });
        final JTextArea textArea = this.lineViewTextArea.getNumerableTextArea();
        this.lineViewTextArea.addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                lines = textArea.getLineCount();
                int offset = e.getOffset();
                int length = e.getLength();
                try {
                    int startLine = textArea.getLineOfOffset(offset);
                    int lastLine = textArea.getLineOfOffset(offset + length);
                    int interval = lastLine - startLine;
                    if (interval > 0) {
                        List<Integer> temp = new LinkedList<Integer>();
                        for (int i : map.keySet()) {
                            if (i > startLine) {
                                temp.add(i);
                            }
                        }
                        for (int i : temp) {
                            toggleBreakpoint(i);
                            toggleBreakpoint(i + interval);
                        }
                    }
                } catch (BadLocationException ex) {
                }
            }

            public void removeUpdate(DocumentEvent e) {
                int offset = e.getOffset();
                int textLines = textArea.getLineCount();
                if (textLines != lines) {
                    try {
                        int startLine = textArea.getLineOfOffset(offset);
                        int endLine = startLine + lines - textLines;
                        List<Integer> temp = new LinkedList<Integer>();
                        for (int i : map.keySet()) {
                            if (i > startLine || i >= textLines) {
                                temp.add(i);
                            }
                        }
                        for (int i : temp) {
                            toggleBreakpoint(i);
                            if (i > endLine) {
                                toggleBreakpoint(i + textLines - lines);
                            }
                        }
                    } catch (BadLocationException ex) {
                    }
                    lines = textLines;
                }
            }

            public void changedUpdate(DocumentEvent e) {
            }
        });
    }
}
