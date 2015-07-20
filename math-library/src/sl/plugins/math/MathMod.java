package sl.plugins.math;

import sl.elements.Type;
import sl.elements.integer.IntegerElement;
import sl.elements.integer.IntegerType;
import sl.plugins.ExternalFunction;
import sl.program.Command;
import sl.program.Program;
import sl.program.ProgramsStack;
import sl.program.Counter;
import sl.plugins.Parameter;
/*
 *  Класс MathMod реализует функцию вычисления остатка от деления целого x на целый y.
 *  @author Полевая Евгения
 */

public class MathMod extends ExternalFunction {

    @Override
    public Type getReturnType() {
        return IntegerType.get();
    }

    @Override
    protected Parameter[] initVariables() {
        return new Parameter[]{
                    new Parameter(IntegerType.get()),
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
                double y = ((IntegerElement) IntegerType.get().
                        convert(stack.pop())).value();
                double x = ((IntegerElement) IntegerType.get().
                        convert(stack.pop())).value();
                stack.push(new IntegerElement((int) (x % y)));
                counter.inc();
            }
        });
        return temp;
    }

    @Override
    public String getName() {
        return "mod";
    }
}
