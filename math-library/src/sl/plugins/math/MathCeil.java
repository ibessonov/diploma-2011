package sl.plugins.math;

import sl.elements.Type;
import sl.elements.integer.IntegerElement;
import sl.elements.integer.IntegerType;
import sl.elements.real.RealElement;
import sl.elements.real.RealType;
import sl.plugins.ExternalFunction;
import sl.program.Command;
import sl.program.Program;
import sl.program.ProgramsStack;
import sl.program.Counter;
import sl.plugins.Parameter;
/*
 *  Класс MathCeil реализует функцию, которая возвращает
 *  ближайшее целое > = данного.
 *  @author Полевая Евгения
 */

public class MathCeil extends ExternalFunction {

    @Override
    public Type getReturnType() {
        return IntegerType.get();
    }

    @Override
    protected Parameter[] initVariables() {
        return new Parameter[]{
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
                double x = ((RealElement) RealType.get().
                        convert(stack.pop())).value();
                stack.push(new IntegerElement((int) Math.ceil(x)));
                counter.inc();
            }
        });
        return temp;
    }

    @Override
    public String getName() {
        return "ceil";
    }
}
