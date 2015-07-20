package sl.ide.io;

import java.io.IOException;
import javax.swing.JTextArea;

public class TextAreaDataWriter implements Appendable {

    private JTextArea area;

    public TextAreaDataWriter(JTextArea area) {
        this.area = area;
    }

    public Appendable append(CharSequence csq) throws IOException {
        area.append(csq.toString());
        return this;
    }

    public Appendable append(CharSequence csq, int start, int end) throws IOException {
        return append(csq.subSequence(start, end));
    }

    public Appendable append(char c) throws IOException {
        return append(c);
    }
}
