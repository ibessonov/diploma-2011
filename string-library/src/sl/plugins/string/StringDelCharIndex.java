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
 *  Класс StringDelCharIndex реализует функцию стр Удал_симв_поз(стр СТРОКА, цел ИНДЕКС),
 *  которая возвращает новую строку, полученную из заданной строки СТРОКА
 *  удалением литерала из позиции ИНДЕКС.
 *  @author Полевая Евгения
 */
public class StringDelCharIndex extends ExternalFunction {

    @Override
    public Type getReturnType() {
        return StringType.get();
    }

    @Override
    protected Parameter[] initVariables() {
        return new Parameter[]{
                    new Parameter(StringType.get()),
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
                IntegerElement ind = (IntegerElement) IntegerType.get().convert(stack.pop());
                StringElement str = (StringElement) StringType.get().convert(stack.pop());
                StringBuilder res = new StringBuilder(str.value());
                stack.push(new StringElement(res.deleteCharAt(ind.value()).toString()));
                counter.inc();
            }
        });
        return temp;
    }

    @Override
    public String getName() {
        return "Удал_симв_поз";
    }
}
