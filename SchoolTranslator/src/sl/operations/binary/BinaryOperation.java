package sl.operations.binary;

public enum BinaryOperation {

    ADDITION("сложение"),
    SUBTRACTION("вычитание"),
    MULTIPLICATION("умножение"),
    DIVISION("деление"),
    CONJUNCTION("конъюнкция"),
    DISJUNCTION("дизъюнкция"),
    COMPARISON_LESS("сравнение <"),
    COMPARISON_NOT_LESS("сравнение >="),
    COMPARISON_GREATER("сравнение >"),
    COMPARISON_NOT_GREATER("сравнение <="),
    COMPARISON_EQUALS("сравнение ="),
    COMPARISON_NOT_EQUALS("сравнение <>");

    private String name;

    private BinaryOperation(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
