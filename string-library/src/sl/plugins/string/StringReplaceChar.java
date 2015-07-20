package sl.plugins.string;

import sl.elements.Type;
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
 *  Класс StringReplaceSub реализует функцию
 *  cтр Зам_симв(лит СТАРЫЙ, лит НОВЫЙ, стр СТРОКА), которая возвращает новую строку,
 *  полученную из строки СТРОКА заменой каждого литерала СТАРЫЙ на литерал НОВЫЙ.
 *  @author Полевая Евгения
 */
public class StringReplaceChar extends ExternalFunction {

    @Override
    public Type getReturnType() {
        return StringType.get();
    }

    @Override
    protected Parameter[] initVariables() {
        return new Parameter[]{
                    new Parameter(CharacterType.get()),
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
                CharacterElement newch = (CharacterElement) CharacterType.get().convert(stack.pop());
                CharacterElement oldch = (CharacterElement) CharacterType.get().convert(stack.pop());
                stack.push(new StringElement(str.value().replace(oldch.value(), newch.value())));
                counter.inc();
            }
        });
        return temp;
    }

    @Override
    public String getName() {
        return "Зам_симв";
    }
}
