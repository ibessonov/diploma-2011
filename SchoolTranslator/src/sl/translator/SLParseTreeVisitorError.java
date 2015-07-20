package sl.translator;

enum SLParseTreeVisitorError {
    TYPE_ALREADY_EXISTS("Тип <%s> уже существует"),
    TYPE_NOT_FOUND("Тип <%s> не найден"),
    RECORD_FIELD_ALREADY_EXISTS("Поле записи <%s#%s> уже существует"),
    RECORD_FIELD_NOT_FOUND("Поле записи <%s#%s> не найдено"),
    FUNCTION_ALREADY_EXISTS("Функция <%s> уже существует"),
    FUNCTION_NOT_FOUND("Функция <%s> не найдена"),
    VARIABLE_ALREADY_EXISTS("Переменная <%s> уже существует"),
    VARIABLE_NOT_FOUND("Переменная <%s> не найдена"),
    UNARY_OPERATION_NOT_SUPPORTED("Операция <%s> не поддерживается для типа <%s>"),
    BINARY_OPERATION_NOT_SUPPORTED("Операция <%s> не поддерживается для типов <%s> и <%s>"),
    EXPRESSION_IN_SWITCH_NOT_INTEGER("Выражение для оператора <выбор> должно быть целым"),
    VARIABLE_IN_FOR_NOT_INTEGER("Переменная для счетчика в цикле <для> должна быть целой"),
    EXPRESSION_FROM_IN_FOR_NOT_INTEGER("Выражение для начальной позиции счетчика в цикле <для> должно быть целым"),
    EXPRESSION_TO_IN_FOR_NOT_INTEGER("Выражение для конечной позиции счетчика в цикле <для> должно быть целым"),
    EXPRESSION_NOT_BOOLEAN("Должно быть использовано логическое выражение"),
    EXPRESSION_NOT_INTEGER("Должно быть использовано целое выражение"),
    MUSTNOT_DO_CONVERTABLE_TYPES_FOR_ASSIGNABLE("Присваивание для типов <%s> и <%s> невозможно"),
    LOWBOUND_IN_ARRAY_MORE_HIGHBOUND("Нижняя граница в массиве не должна быть больше верхней"),
    RETURN_STATEMENT_IN_FUNCTION_IS_ABSENT("Функция должна возвращать значение"),
    RETURN_STATEMENT_IN_FUNCTION_IS_AXCESS("Функция не должна возвращать значение"),
    NUMBER_OF_PARAMETERS_WRONG("Не совпадает число параметров функции <%s>: найдено %s, должно быть %s"),
    MUSTNOT_SENT_CONSTANT_BY_LINK("Нельзя передавать константу по ссылке"),
    WRONG_PARAMETER_BY_LINK("Недопустимый параметр по ссылке"),
    WRONG_TYPE_OF_PARAMETER("Неправильный тип параметра: должен быть <%s>, имеется <%s>"),
    WRONG_RETURN_TYPE("Неправильный тип возвращаемого значения: должен быть <%s>, имеется <%s>"),
    EXPRESSION_MUST_BE_ARRAY_TYPE("Обращение по индексу невозможно для типа <%s>"),
    EXPRESSION_MUST_BE_RECORD_TYPE("Обращение к полю невозможно для типа <%s>"),
    DIMENTIONS_OF_ARRAY_IS_WRONG("Количество индексов не совпадает с размерностью массива: должно быть %s, имеется %s"),
    COMPILE_ERROR("FATAL ERROR"),
    ;

    private String name;

    private SLParseTreeVisitorError(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
