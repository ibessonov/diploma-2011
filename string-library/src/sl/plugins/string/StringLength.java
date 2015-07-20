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
 *  Класс StringLenght реализует функцию цел Длина(стр СТРОКА), которая возвращает
 *  количество символов в строке СТРОКА.
 *  @author Полевая Евгения
 */

public class StringLength extends ExternalFunction {

    @Override
    public Type getReturnType() {
        return IntegerType.get();
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
                stack.push(new IntegerElement(top.value().length()));
                counter.inc();
            }
        });
        return temp;
    }

    @Override
    public String getName() {
        return "Длина";
    }
}
