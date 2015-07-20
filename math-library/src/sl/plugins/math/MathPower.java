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
 *  Класс MathPower реализует функцию возведения целого положительного числа
 *  в неотрицательную степень. Если степень < 0, то функция возвращает 0.
 *  @author Полевая Евгения
 */

public class MathPower extends ExternalFunction {

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
                int y = ((IntegerElement) IntegerType.get().
                        convert(stack.pop())).value();
                int x = ((IntegerElement) IntegerType.get().
                        convert(stack.pop())).value();
                if (y < 0 || x <= 0) {
                    stack.push(new IntegerElement(0));
                } else if (y == 0) {
                    stack.push(new IntegerElement(1));
                } else {
                    while (y > 0) {
                        x *= x;
                        y--;
                    }
                }
                stack.push(new IntegerElement(x));
                counter.inc();
            }
        });
        return temp;
    }

    @Override
    public String getName() {
        return "pow";
    }
}
