package sl.plugins;

import sl.program.Program;
import sl.program.commands.ReturnCommand;

/**
 * Абстрактный базовый класс для всех внешних функций
 * @author Полевая Евгения
 */
public abstract class ExternalFunction implements Function {

    private Program program;
    private Parameter[] vars;

    /**
     * Метод, инициализирующий набор параметров функции. Перегружается у всех
     * наследников
     * @return Массив, содержащий параметры функции
     */
    protected abstract Parameter[] initVariables();

    /**
     * Метод, инициализирующий код функции в промежуточном представлении. Он
     * должен быть представлен отдельной программой, предполагающей наличие
     * параметров в программном стеке. Наличия команды {@link ReturnCommand}
     * не требуется. По окончании работы функции все входные параметры должны
     * быть извлечены. На вершине программного стека должно быть возвращаемое
     * значение, если оно требуется
     * @return Код функции в промежуточном представлении
     */
    protected abstract Program initProgram();

    @Override
    public final int getParamsCount() {
        return getParameters().length;
    }

    @Override
    public final Parameter getParameter(int index) {
        return getParameters()[index];
    }

    @Override
    public final Program getProgram() {
        return (program == null) ? (program = initProgram()) : program;
    }

    private Parameter[] getParameters() {
        return (vars == null) ? (vars = initVariables()) : vars;
    }
}
