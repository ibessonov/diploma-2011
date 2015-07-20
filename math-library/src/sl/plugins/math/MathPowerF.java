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
 *  Класс MathPowerF реализует функцию возведения вещ. числа в вещ. степень.
 *  @author Полевая Евгения
 */

public class MathPowerF extends ExternalFunction {

    @Override
    public Type getReturnType() {
        return RealType.get();
    }

    @Override
    protected Parameter[] initVariables() {
        return new Parameter[]{
                    new Parameter(RealType.get()),
                    new Parameter(RealType.get())
                };
    }

    @Override
    protected final Program initProgram() {
        Program temp = new Program();
        temp.append(new Command() {

            @Override
            public void execute(ProgramsStack stack, Counter counter)
                    throws Exception {
                double y = ((RealElement) RealType.get().
                        convert(stack.pop())).value();
                double x = ((RealElement) RealType.get().
                        convert(stack.pop())).value();
                stack.push(new RealElement(Math.pow(x, y)));
                counter.inc();
            }
        });
        return temp;
    }

    @Override
    public String getName() {
        return "powf";
    }
}
