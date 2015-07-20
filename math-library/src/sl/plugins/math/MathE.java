package sl.plugins.math;

import sl.elements.Type;
import sl.elements.real.RealElement;
import sl.elements.real.RealType;
import sl.plugins.ExternalFunction;
import sl.program.Command;
import sl.program.Program;
import sl.program.ProgramsStack;
import sl.program.Counter;
import sl.plugins.Parameter;
/*
 *  Класс MathE реализует функцию получения числа E.
 *  @author Полевая Евгения
 */

public class MathE extends ExternalFunction {

    @Override
    public Type getReturnType() {
        return RealType.get();
    }

    @Override
    protected Parameter[] initVariables() {
        return new Parameter[]{};
    }

    @Override
    protected final Program initProgram() {
        Program temp = new Program();
        temp.append(new Command() {

            @Override
            public void execute(ProgramsStack stack, Counter counter)
                    throws Exception {
                stack.push(new RealElement(Math.E));
                counter.inc();
            }
        });
        return temp;
    }

    @Override
    public String getName() {
        return "E";
    }
}