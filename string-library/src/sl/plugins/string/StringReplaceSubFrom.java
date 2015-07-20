package sl.plugins.string;

import sl.elements.Type;
import sl.elements.string.StringElement;
import sl.elements.string.StringType;
import sl.elements.integer.IntegerElement;
import sl.elements.integer.IntegerType;
import sl.plugins.ExternalFunction;
import sl.program.Command;
import sl.program.Program;
import sl.program.ProgramsStack;
import sl.program.Counter;
import sl.plugins.Parameter;
/*
 *  Класс StringReplaceSubFrom реализует функцию
 *  cтр Зам_подстр_от(стр ПОДСТР,цел ИНДЕКС,стр СТРОКА), которая возвращает новую строку,
 *  полученную из строки СТРОКА заменой подстроки ПОДСТР, начиная с позицию ИНДЕКС.
 *  @author Полевая Евгения
 */

public class StringReplaceSubFrom extends ExternalFunction {

    @Override
    public Type getReturnType() {
        return StringType.get();
    }

    @Override
    protected Parameter[] initVariables() {
        return new Parameter[]{
                    new Parameter(StringType.get()),
                    new Parameter(IntegerType.get()),
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
                StringElement str = (StringElement) StringType.get().convert(stack.pop());
                IntegerElement from = (IntegerElement) IntegerType.get().convert(stack.pop());
                StringElement replacement = (StringElement) StringType.get().convert(stack.pop());
                stack.push(new StringElement((str.value().substring(0, from.value()))
                        + replacement.value()));
                counter.inc();
            }
        });
        return temp;
    }

    @Override
    public String getName() {
        return "Зам_подстр_от";
    }
}
