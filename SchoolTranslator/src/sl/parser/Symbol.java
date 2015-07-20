package sl.parser;

final class Symbol {

    private Symbol() {
    }

    public static boolean isSpace(char c) {
        return (c == ' ' || c == '\t' || c == '\r' || c == '\n');
    }

    public static boolean isDigit(char c) {
        return ('0' <= c && c <= '9');
    }

    public static boolean isLetter(char c) {
        return ('a' <= c && c <= 'z')
                || ('A' <= c && c <= 'Z')
                || ('а' <= c && c <= 'я')
                || ('А' <= c && c <= 'Я')
                || (c == 'ё')
                || (c == 'Ё');
    }

    public static boolean isIdentifierBegin(char c) {
        return isLetter(c) || c == '_';
    }

    public static boolean isIdentifierPart(char c) {
        return isDigit(c) || isIdentifierBegin(c);
    }
}
