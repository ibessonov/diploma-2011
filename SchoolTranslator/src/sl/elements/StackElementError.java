package sl.elements;

public enum StackElementError {

    TYPE_CONVERTION_NOT_SUPPORTED("Тип <%s> не допускает преобразования"),
    TYPE_OPERATION_NOT_SUPPORTED("Тип <%s> на допускает операцию <%s>"),
    TYPE_ASSIGNMENT_NOT_SUPPORTED("Тип <%s> на допускает операцию присваивания"),
    TYPE_READ_NOT_SUPPORTED("Тип <%s> на допускает чтение"),
    TYPE_WRITE_NOT_SUPPORTED("Тип <%s> на допускает вывод"),
    ASSIGNMENT_TO_CONSTANT("Запрещено присваивать константам"),
    ;
    private String name;

    private StackElementError(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
