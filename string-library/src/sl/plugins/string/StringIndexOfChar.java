package sl.plugins.string;

import sl.elements.Type;
import sl.elements.integer.IntegerElement;
import sl.elements.integer.IntegerType;
import sl.elements.string.StringElement;
import sl.elements.character.CharacterElement;
import sl.elements.character.CharacterType;
import sl.elements.string.StringType;
import sl.plugins.ExternalFunction;
import sl.program.Command;
import sl.program.Program;
import sl.program.ProgramsStack;
import sl.program.Counter;
import sl.plugins.Parameter;

/*
 *  Класс StringIndexOfChar реализует функцию цел Поз_симв(лит СИМВ, стр СТРОКА),
 *  которая возвращает индекс первого литерала СИМВ в строке СТРОКА.
 *  Если объект не содержит данный символ, то возвращается значение -1.
 *  @author Полевая Евгения
 */
public class StringIndexOfChar extends ExternalFunction {

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
                stack.push(new IntegerElement(str.value().indexOf(ch.value())));
                counter.inc();
            }
        });
        return temp;
    }

    @Override
    public String getName() {
        return "Поз_симв";
    }
}
