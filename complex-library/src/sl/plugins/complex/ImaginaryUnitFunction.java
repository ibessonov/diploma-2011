package sl.plugins.complex;

import sl.elements.Type;
import sl.plugins.ExternalFunction;
import sl.program.Program;
import sl.program.commands.PushConstantCommand;
import sl.plugins.Parameter;

public class ImaginaryUnitFunction extends ExternalFunction {

    public Type getReturnType() {
        return ComplexType.get();
    }

    @Override
    protected Parameter[] initVariables() {
        return new Parameter[]{};
    }

    @Override
    protected Program initProgram() {
        Program temp = new Program();
        temp.append(new PushConstantCommand(ComplexElement.IMAGINARY_UNIT));
        return temp;
    }

    public String getName() {
        return "i";
    }
}
