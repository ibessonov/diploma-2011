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
 *  Класс StringIndexOfSub реализует функцию цел Поз_подстр(стр  ПОДСТР, стр СТРОКА),
 *  которая возвращает  индекс первого символа первого вхождения подстроки
 *  ПОДСТР в строку СТРОКА. Если объект не содержит данной подстроки,
 *  то возвращается значение -1.
 *  @author Полевая Евгения
 */
public class StringIndexOfSub extends ExternalFunction {

    @Override
    public Type getReturnType() {
        return IntegerType.get();
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
                StringElement str = (StringElement) StringType.get().convert(stack.pop());
                StringElement sub = (StringElement) StringType.get().convert(stack.pop());
                stack.push(new IntegerElement(str.value().indexOf(sub.value())));
                counter.inc();
            }
        });
        return temp;
    }

    @Override
    public String getName() {
        return "Поз_подстр";
    }
}
