package sl.translator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import sl.program.DebugInformation;

/**
 *
 * @author Бессонов Иван
 */
class SLDebugInformation implements DebugInformation {

    private List<Integer> rows = new ArrayList<Integer>();
    private Map<Integer, String> functions = new HashMap<Integer, String>();
    private Map<String, Map<String, Variable>> variables = new HashMap<String, Map<String, Variable>>();

    public SLDebugInformation() {
    }

    @Override
    public int getCommandRow(int index) {
        return rows.get(index);
    }

    @Override
    public String getFunctionName(int index) {
        return functions.get(index);
    }

    @Override
    public Set<String> getVariables(String function) {
        Map<String, Variable> map = variables.get(function);
        return (map == null) ? null : map.keySet();
    }

    @Override
    public Integer getVariableOffset(String function, String variable) {
        Map<String, Variable> map = variables.get(function);
        return (map == null) ? null : map.get(variable).getOffset();
    }

    public void addVariable(String function, String name, Variable variable) {
        variables.get(function).put(name, variable);
    }

    public void addFunction(String name, int index) {
        functions.put(index, name);
        variables.put(name, new HashMap<String, Variable>());
    }

    public void addCommandRow(int row) {
        rows.add(row);
    }
}
