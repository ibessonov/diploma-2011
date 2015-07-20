package sl.plugins.string;

import sl.elements.Type;
import sl.elements.integer.IntegerElement;
import sl.elements.integer.IntegerType;
import sl.elements.string.StringElement;
import sl.elements.string.StringType;
import sl.plugins.ExternalFunction;
import sl.program.Command;
import sl.program.Program;
import sl.program.ProgramsStack;
import sl.program.Counter;
import sl.plugins.Parameter;
/*
 *  Класс StringSubFromTo реализует функцию
 *  стр Подстр_отдо (стр СТРОКА, цел НАЧ_ИНДЕКС, цел КОН_ИНДЕКС), которая
 *  возвращает новую строку, полученную из строки СТРОКА начинающуюся
 *  с позиции НАЧ_ИНДЕКС и до позиции КОН_ИНДЕКС. Длина подстроки = КОН_ИНДЕКС-НАЧ_ИНДЕКС.
 *  @author Полевая Евгения
 */

public class StringSubFromTo extends ExternalFunction {

    @Override
    public Type getReturnType() {
        return StringType.get();
    }

    @Override
    protected Parameter[] initVariables() {
        return new Parameter[]{
                    new Parameter(StringType.get()),
                    new Parameter(IntegerType.get()),
                    new Parameter(IntegerType.get())
                };
    }

    @Override
    protected final Program initProgram() {
        Program temp = new Program();
        temp.append(new Command() {

            @Override
            public void execute(ProgramsStack stack, Counter counter)
                    throws Exception {
                IntegerElement to = (IntegerElement) IntegerType.get().convert(stack.pop());
                IntegerElement from = (IntegerElement) IntegerType.get().convert(stack.pop());
                StringElement str = (StringElement) StringType.get().convert(stack.pop());
                stack.push(new StringElement(str.value().substring(from.value(), to.value())));
                counter.inc();
            }
        });
        return temp;
    }

    @Override
    public String getName() {
        return "Подстр_отдо";
    }
}
