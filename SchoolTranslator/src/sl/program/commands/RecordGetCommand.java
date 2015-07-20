package sl.program.commands;

import sl.elements.record.RecordElement;
import sl.program.Command;
import sl.program.ProgramsStack;
import sl.program.Counter;

public class RecordGetCommand implements Command {

    private String field;

    public RecordGetCommand(String field) {
        this.field = field;
    }

    @Override
    public void execute(ProgramsStack stack, Counter counter) throws Exception {
        RecordElement record = (RecordElement) stack.pop();
        stack.push(record.get(field));
        counter.inc();
    }

    @Override
    public String toString() {
        return "взять поле записи" + field;
    }
}
