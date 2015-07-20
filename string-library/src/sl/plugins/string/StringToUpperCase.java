package sl.plugins.string;

import sl.elements.Type;
import sl.elements.string.StringElement;
import sl.elements.string.StringType;
import sl.plugins.ExternalFunction;
import sl.program.Command;
import sl.program.Program;
import sl.program.ProgramsStack;
import sl.program.Counter;
import sl.plugins.Parameter;
/*
 *  Класс StringToUpperCase реализует функцию стр Заглав(стр СТРОКА), которая
 *  возвращает новую строку, полученную из строки СТРОКА заменой всех букв на заглавные.
 *  @author Полевая Евгения
 */

public class StringToUpperCase extends ExternalFunction {

    @Override
    public Type getReturnType() {
        return StringType.get();
    }

    @Override
    protected Parameter[] initVariables() {
        return new Parameter[]{
                    new Parameter(StringType.get())
                };
    }

    @Override
    protected final Program initProgram() {
        Program temp = new Program();
        temp.append(new Command() {

            @Override
            public void execute(ProgramsStack stack, Counter counter)
                    throws Exception {
                StringElement top = (StringElement) StringType.get().convert(stack.pop());
                stack.push(new StringElement(top.value().toUpperCase()));
                counter.inc();
            }
        });
        return temp;
    }

    @Override
    public String getName() {
        return "Заглав";
    }
}
