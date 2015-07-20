package sl.plugins.string;

import sl.elements.Type;
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
 *  Класс StringDelChar реализует функцию стр Удал_симв(лит СИМВ, стр СТРОКА),
 *  которая возвращает новую строку, полученную из заданной строки СТРОКА
 *  удалением литерала СИМВ.
 *  @author Полевая Евгения.
 */
public class StringDelChar extends ExternalFunction {

    @Override
    public Type getReturnType() {
        return StringType.get();
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
                StringBuilder res = new StringBuilder(str.value());
                String strCH = ch.toString();
                while (res.indexOf(strCH) >= 0) {
                    res.deleteCharAt(res.indexOf(strCH));
                }
                stack.push(new StringElement(res.toString()));
                counter.inc();
            }
        });
        return temp;
    }

    @Override
    public String getName() {
        return "Удал_симв";
    }
}
