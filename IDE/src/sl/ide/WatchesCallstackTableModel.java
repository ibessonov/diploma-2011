package sl.ide;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author 
 */
class WatchesCallstackTableModel extends DefaultTableModel {

    public WatchesCallstackTableModel(Object[][] data) {
        super(data, columnNames);
    }
    private static String columnNames[] = {
        "Стек вызовов"
    };
    private Class[] types = new Class[]{
        java.lang.String.class
    };
    private boolean[] canEdit = new boolean[]{
        false
    };

    @Override
    public Class getColumnClass(int columnIndex) {
        return types[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEdit[columnIndex];
    }
}
