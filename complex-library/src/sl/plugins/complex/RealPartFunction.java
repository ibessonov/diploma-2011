package sl.plugins.complex;

import sl.elements.Type;
import sl.elements.real.RealElement;
import sl.elements.real.RealType;
import sl.program.Command;
import sl.program.Counter;
import sl.program.Program;
import sl.program.ProgramsStack;
import sl.plugins.ExternalFunction;
import sl.plugins.Parameter;

public class RealPartFunction extends ExternalFunction {

    @Override
    protected Parameter[] initVariables() {
        return new Parameter[]{
                    new Parameter(ComplexType.get())
                };
    }

    @Override
    protected Program initProgram() {
        Program program = new Program();
        program.append(new Command() {

            public void execute(ProgramsStack stack, Counter counter)
                    throws Exception {
                ComplexElement e = (ComplexElement) ComplexType.get().
                        convert(stack.pop());
                stack.push(new RealElement(e.real()));
                counter.inc();
            }
        });
        return program;
    }

    public String getName() {
        return "вещ_часть";
    }

    public Type getReturnType() {
        return RealType.get();
    }
}
