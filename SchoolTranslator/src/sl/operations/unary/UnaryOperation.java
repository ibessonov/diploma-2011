package sl.operations.unary;

public enum UnaryOperation {

    MINUS("смена знака"),
    NEGATION("отрицание");

    private String name;

    private UnaryOperation(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
