package sl.ide;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import sl.elements.StackElement;
import sl.elements.string.StringElement;
import sl.elements.string.StringType;
import sl.program.DebugInformation;
import sl.program.ProgramsStack;

/**
 *
 * @author
 */
public class WatchesManager {

    private JTable callStack;
    private JTable watchesTable;
    private ProgramsStack programsStack = null;
    private DebugInformation debugInfo = null;
    private ListSelectionListener listener = new ListSelectionListener() {

        public void valueChanged(ListSelectionEvent e) {
            updateWatchesModel();
        }
    };

    public WatchesManager(JTable callStack, JTable watchesTable) {
        this.callStack = callStack;
        this.watchesTable = watchesTable;
        callStack.getSelectionModel().addListSelectionListener(listener);
    }

    public void setInfo(ProgramsStack ps, DebugInformation di) {
        programsStack = ps;
        debugInfo = di;
        Iterator<Integer> iterator = ps.callStack();
        List<Object[]> namesList = new ArrayList<Object[]>();
        while (iterator.hasNext()) {
            namesList.add(0, new Object[]{
                        debugInfo.getFunctionName(iterator.next())
                    });
        }
        callStack.setModel(new WatchesCallstackTableModel(
                namesList.toArray(new Object[][]{})));
        if (callStack.getRowCount() > 0) {
            ListSelectionModel selectionModel = callStack.getSelectionModel();
            selectionModel.addListSelectionListener(listener);
            selectionModel.setSelectionInterval(0, 0);
        }
    }

    private void updateWatchesModel() {
        int selectedRow = callStack.getSelectedRow();
        if (selectedRow < 0) {
            return;
        }
        String functionName = (String) callStack.getValueAt(selectedRow, 0);
        selectedRow = callStack.getRowCount() - 1 - selectedRow;
        int functionOffset = programsStack.offset(selectedRow);
        List<Object[]> watchesList = new ArrayList<Object[]>();

        for (String variableName : debugInfo.getVariables(functionName)) {
            StackElement variable = programsStack.element(
                    functionOffset
                    + debugInfo.getVariableOffset(functionName, variableName));
            String variableValue = null;
            try {
                variableValue = variable.toString();
            } catch (Throwable t) {
                variableValue = "";
            }
            watchesList.add(new Object[]{
                        variableName, variable.type().toString(), variableValue
                    });
        }
        watchesTable.setModel(new WatchesVariablesTableModel(
                watchesList.toArray(new Object[][]{})));
    }
}
