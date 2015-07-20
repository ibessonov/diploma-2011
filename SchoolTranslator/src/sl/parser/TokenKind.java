package sl.parser;

public enum TokenKind {
    EOF("<конец файла>"),
    SINGLE_LINE_COMMENT("<комментарий>"),
    INTEGER_LITERAL("<целое число>"),
    FLOATING_POINT_LITERAL("<вещественное число>"),
    CHARACTER_LITERAL("<символьный литерал>"),
    STRING_LITERAL("<строковый литерал>"),
    ALGORITHM("алг"),
    BEGIN("нач"),
    END("кон"),
    TYPE("тип"),
    TABLE("таб"),
    RECORD("запись"),
    INTEGER("цел"),
    REAL("вещ"),
    CHAR("лит"),
    STRING("стр"),
    BOOLEAN("лог"),
    READ("ввод"),
    WRITE("вывод"),
    SWITCH("выбор"),
    CASE("при"),
    ALL("все"),
    IF("если"),
    THEN("то"),
    ELSE("иначе"),
    BEGINCYCLE("нц"),
    ENDCYCLE("кц"),
    WHILE("пока"),
    FOR("для"),
    FROM("от"),
    TO("до"),
    RESULT("рез"),
    ARGUMENT("арг"),
    RETURN("возврат"),
    TRUE("да"),
    FALSE("нет"),
    OR("или"),
    AND("и"),
    NOT("не"),
    IDENTIFIER("<идентификатор>"),
    PLUS("+"),
    MINUS("-"),
    MULTIPLY("*"),
    DIVIDE("/"),
    LESS("<"),
    LESS_OR_EQUALS("<="),
    GREATER(">"),
    GREATER_OR_EQUALS(">="),
    EQUALS("="),
    NOT_EQUALS("<>"),
    ASSIGN(":="),
    SEMICOLON(";"),
    OBRACKET("["),
    COLON(":"),
    COMMA(","),
    CBRACKET("]"),
    OPARENTHESIS("("),
    CPARENTHESIS(")"),
    DOT("."),
    DEFAULT("");

    private String name;

    private TokenKind(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
