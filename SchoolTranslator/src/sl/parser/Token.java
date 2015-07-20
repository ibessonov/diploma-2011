package sl.parser;

public class Token {

    private TokenKind kind;
    private TokenPosition position;
    private String image;
    private Token nextToken;
    private Token specialToken;

    public Token(TokenKind kind, String image, TokenPosition position) {
        this.kind = kind;
        this.position = position;
        this.image = image;
        this.nextToken = null;
        this.specialToken = null;
    }

    public TokenKind getKind() {
        return kind;
    }

    public TokenPosition getPosition() {
        return position;
    }

    public String getImage() {
        return image;
    }

    public Token getNextToken() {
        return nextToken;
    }

    public Token getSpecialToken() {
        return specialToken;
    }

    public void setNextToken(Token next) {
        this.nextToken = next;
    }

    public void setSpecialToken(Token special) {
        this.specialToken = special;
    }
}
