package sl.parser;

public class TokenPosition {

    private int beginLine;
    private int beginColumn;
    private int endLine;
    private int endColumn;
    private int beginPosition;
    private int endPosition;

    public TokenPosition(int beginLine, int beginColumn,
            int endLine, int endColumn,
            int beginPosition, int endPosition) {
        this.beginLine = beginLine;
        this.beginColumn = beginColumn;
        this.endLine = endLine;
        this.endColumn = endColumn;
        this.beginPosition = beginPosition;
        this.endPosition = endPosition;
    }

    public int getBeginLine() {
        return beginLine;
    }

    public int getBeginColumn() {
        return beginColumn;
    }

    public int getEndLine() {
        return endLine;
    }

    public int getEndColumn() {
        return endColumn;
    }

    public int getBeginPosition() {
        return beginPosition;
    }

    public int getEndPosition() {
        return endPosition;
    }
}
