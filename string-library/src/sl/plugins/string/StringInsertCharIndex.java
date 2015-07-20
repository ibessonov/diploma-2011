package sl.plugins.string;

import sl.elements.Type;
import sl.elements.character.CharacterElement;
import sl.elements.character.CharacterType;
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
 *  Класс StringInsertCharIndex реализует функцию 
 *  стр Вст_поз_симв(лит СИМВ, цел ИНДЕКС, стр СТРОКА), которая возвращает новую строку,
 *  полученную из строки СТРОКА добавлением литерала СИМВ в позицию ИНДЕКС.
 *  @author Полевая Евгения
 */
public class StringInsertCharIndex extends ExternalFunction {

    @Override
    public Type getReturnType() {
        return StringType.get();
    }

    @Override
    protected Parameter[] initVariables() {
        return new Parameter[]{
                    new Parameter(CharacterType.get()),
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
                IntegerElement ind = (IntegerElement) IntegerType.get().convert(stack.pop());
                CharacterElement ch = (CharacterElement) CharacterType.get().convert(stack.pop());
                StringBuilder res = new StringBuilder(str.value());
                stack.push(new StringElement(res.insert(ind.value(), ch.value()).toString()));
                counter.inc();
            }
        });
        return temp;
    }

    @Override
    public String getName() {
        return "Вст_поз_симв";
    }
}
