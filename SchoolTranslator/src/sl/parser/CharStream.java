package sl.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

public class CharStream {

    private int position;
    private Reader reader;
    private int tabSize = 4;
    private Buffer buffer;
    private Stack backupStack;

    public CharStream(InputStream stream) {
        this(new InputStreamReader(stream));
    }

    public CharStream(InputStream stream, String encoding)
            throws UnsupportedEncodingException {
        this(encoding == null ? new InputStreamReader(stream)
                : new InputStreamReader(stream, encoding));
    }

    private CharStream(Reader stream) {
        reInit(stream);
    }

    public void reInit(InputStream dstream) {
        reInit(new InputStreamReader(dstream));
    }

    public void reInit(InputStream stream, String encoding)
            throws UnsupportedEncodingException {
        reInit(encoding == null ? new InputStreamReader(stream)
                : new InputStreamReader(stream, encoding));
    }

    private void reInit(Reader stream) {
        position = 0;
        reader = stream;
        buffer = new Buffer();
        backupStack = new Stack();
    }

    public void setTabSize(int size) {
        tabSize = size;
    }

    public char begin() throws IOException {
        char c = backupStack.empty()
                ? (char) reader.read()
                : backupStack.pop();
        buffer.reInit(c);
        ++position;
        return c;
    }

    public char read() throws IOException {
        char c = backupStack.empty()
                ? (char) reader.read()
                : backupStack.pop();
        buffer.append(c);
        ++position;
        return c;
    }

    public char current() {
        return backupStack.empty()
                ? buffer.current()
                : backupStack.top();
    }

    public int count() {
        return buffer.count();
    }

    public TokenPosition position() {
        return buffer.position();
    }

    public boolean backup(int amount) {
        if (amount >= buffer.count()) {
            return false;
        }
        for (int i = 0; i < amount; ++i) {
            backupStack.push(buffer.backup());
        }
        position -= amount;
        return true;
    }

    public String image() {
        return buffer.image();
    }

    private class Buffer extends ExpandableCharArray {

        private int[] lines;
        private int[] columns;

        public Buffer() {
            super();
            lines = new int[INIT_SIZE];
            columns = new int[INIT_SIZE];
        }

        public void append(char c) {
            if (++pos == size) {
                expand();
            }
            buffer[pos] = c;
            setupPosition(buffer[pos - 1], lines[pos - 1], columns[pos - 1]);
        }

        public char backup() {
            return buffer[pos--];
        }

        public char current() {
            return buffer[pos];
        }

        public void reInit(char c) {
            if (pos == -1) {
                pos = 0;
                buffer[0] = c;
                lines[0] = 1;
                columns[0] = 1;
            } else {
                char d = buffer[pos];
                int line = lines[pos];
                int column = columns[pos];
                buffer[0] = c;
                pos = 0;
                setupPosition(d, line, column);
            }
        }

        public int count() {
            return pos + 1;
        }

        public String image() {
            return new String(buffer, 0, pos + 1);
        }

        public TokenPosition position() {
            return new TokenPosition(lines[0], columns[0],
                    lines[pos], columns[pos],
                    position - count(), position);
        }

        private void setupPosition(char c, int line, int column) {
            ++column;
            if (c == '\n' || c == '\r' && buffer[pos] != '\n') {
                ++line;
                column = 1;
            }
            if (buffer[pos] == '\t') {
                --column;
                column += (tabSize - (column % tabSize));
            }
            lines[pos] = line;
            columns[pos] = column;
        }

        @Override
        protected void expand() {
            int newLines[] = new int[size + EXPAND_SIZE];
            int newColumns[] = new int[size + EXPAND_SIZE];
            System.arraycopy(lines, 0, newLines, 0, size);
            lines = newLines;
            System.arraycopy(columns, 0, newColumns, 0, size);
            columns = newColumns;
            super.expand();
        }
    }

    private class Stack extends ExpandableCharArray {

        public boolean empty() {
            return (pos == -1);
        }

        public void push(char c) {
            if (++pos == size) {
                expand();
            }
            buffer[pos] = c;
        }

        public char pop() {
            return buffer[pos--];
        }

        public char top() {
            return buffer[pos];
        }
    }
}

abstract class ExpandableCharArray {

    protected static final int INIT_SIZE = 128;
    protected static final int EXPAND_SIZE = 64;
    protected char[] buffer;
    protected int pos;
    protected int size;

    public ExpandableCharArray() {
        pos = -1;
        size = INIT_SIZE;
        buffer = new char[INIT_SIZE];
    }

    protected void expand() {
        char newBuffer[] = new char[size + EXPAND_SIZE];
        System.arraycopy(buffer, 0, newBuffer, 0, size);
        buffer = newBuffer;
        size += EXPAND_SIZE;
    }
}