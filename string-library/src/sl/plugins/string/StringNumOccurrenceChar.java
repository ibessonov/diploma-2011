package sl.plugins.string;

import sl.elements.Type;
import sl.elements.integer.IntegerElement;
import sl.elements.integer.IntegerType;
import sl.elements.character.CharacterElement;
import sl.elements.character.CharacterType;
import sl.elements.string.StringElement;
import sl.elements.string.StringType;
import sl.plugins.ExternalFunction;
import sl.program.Command;
import sl.program.Program;
import sl.program.ProgramsStack;
import sl.program.Counter;
import sl.plugins.Parameter;
/*
 *  Класс StringNumOccurrenceChar реализует функцию цел Симв_стр(лит СИМВ, стр СТРОКА),
 *  которая считает количество вхождений литерала СИМВ в строке СТРОКА.
 *  @author Полевая Евгения
 */

public class StringNumOccurrenceChar extends ExternalFunction {

    @Override
    public Type getReturnType() {
        return IntegerType.get();
    }

    @Override
    protected Parameter[] initVariables() {
        return new Parameter[]{
                    new Parameter(CharacterType.get()),
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
                CharacterElement ch = (CharacterElement) CharacterType.get().convert(stack.pop());
                if (str.value().indexOf(ch.value()) == -1) {
                    stack.push(new IntegerElement(0));
                } else {
                    int k, num = 0;
                    int len = str.value().length();
                    k = str.value().indexOf(ch.value());
                    num++;
                    while (k != len - 1) {
                        k = str.value().indexOf(ch.value(), ++k);
                        num++;
                    }
                    stack.push(new IntegerElement(num));
                }
                counter.inc();
            }
        });
        return temp;
    }

    @Override
    public String getName() {
        return "Симв_стр";
    }
}
