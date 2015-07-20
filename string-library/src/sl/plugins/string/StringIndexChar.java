package sl.plugins.string;

import sl.elements.Type;
import sl.elements.integer.IntegerElement;
import sl.elements.integer.IntegerType;
import sl.elements.string.StringElement;
import sl.elements.character.CharacterElement;
import sl.elements.string.StringType;
import sl.plugins.ExternalFunction;
import sl.program.Command;
import sl.program.Program;
import sl.program.ProgramsStack;
import sl.program.Counter;
import sl.plugins.Parameter;

/*
 *  Класс StringIndexChar реализует функцию лит Симв_поз(стр СТРОКА, цел ИНДЕКС)
 *  которая возвращает литерал из строки строка из позиции ИНДЕКС.
 *  @author Полевая Евгения
 */
public class StringIndexChar extends ExternalFunction {

    @Override
    public Type getReturnType() {
        return IntegerType.get();
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
                stack.push(new CharacterElement(str.value().charAt(ind.value())));
                counter.inc();
            }
        });
        return temp;
    }

    @Override
    public String getName() {
        return "Симв_поз";
    }
}
