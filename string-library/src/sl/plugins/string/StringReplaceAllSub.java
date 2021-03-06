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
 *  Класс StringReplaceAllSub реализует функцию
 *  стр Зам_подстр_все(стр СТАРАЯ, стр НОВАЯ, стр СТРОКА), которая возвращает новую строку,
 *  полученную из строки СТРОКА заменой каждой подстроки СТАРАЯ на подстроку НОВАЯ.
 *  @author Полевая Евгения
 */

public class StringReplaceAllSub extends ExternalFunction {

    @Override
    public Type getReturnType() {
        return StringType.get();
    }

    @Override
    protected Parameter[] initVariables() {
        return new Parameter[]{
                    new Parameter(StringType.get()),
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
                StringElement new_str = (StringElement) StringType.get().convert(stack.pop());
                StringElement old_str = (StringElement) StringType.get().convert(stack.pop());
                stack.push(new StringElement(str.value().replace(old_str.value(), new_str.value())));
                counter.inc();
            }
        });
        return temp;
    }

    @Override
    public String getName() {
        return "Зам_подстр_все";
    }
}