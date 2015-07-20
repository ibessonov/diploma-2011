package sl.parser;

public enum ParseError {
    
    ERROR_READING_INPUT_STREAM("Ошибка чтения входного потока"),
    WRONG_NUMBER_FORMAT("Неверный формат числа"),
    WRONG_CHARACTER_FORMAT("Неверный формат символьного литерала"),
    UNEXPECTED_END_OF_FILE("Неожиданный конец файла"),
    UNEXPECTED_END_OF_STRING("Неожиданный конец строки"),
    WRONG_ESCAPE_SEQUENCE("Неверный формат символа"),
    UNKNOWN_SYMBOL("Неизвестный символ");

    private String name;

    private ParseError(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
