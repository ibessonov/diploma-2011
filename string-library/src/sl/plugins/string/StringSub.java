package sl.plugins.string;

import sl.elements.Type;
import sl.elements.bool.BooleanElement;
import sl.elements.bool.BooleanType;
import sl.elements.string.StringElement;
import sl.elements.string.StringType;
import sl.plugins.ExternalFunction;
import sl.program.Command;
import sl.program.Program;
import sl.program.ProgramsStack;
import sl.program.Counter;
import sl.plugins.Parameter;
/*
 *  Класс StringSub реализует функцию лог Подстр(стр ПОДСТР, стр СТРОКА),
 *  которая возвращает логическую переменную да, если подстрока ПОДСТР
 *  принадлежит строке СТРОКА, и нет - иначе.
 *  @author Полевая Евгения
 */

public class StringSub extends ExternalFunction {

    @Override
    public Type getReturnType() {
        return BooleanType.get();
    }

    @Override
    protected Parameter[] initVariables() {
        return new Parameter[]{
                    new Parameter(StringType.get()),
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
                StringElement sub = (StringElement) StringType.get().convert(stack.pop());
                StringElement str = (StringElement) StringType.get().convert(stack.pop());
                stack.push(new BooleanElement(str.value().indexOf(sub.value()) >= 0));
                counter.inc();
            }
        });
        return temp;
    }

    @Override
    public String getName() {
        return "Подстр";
    }
}
