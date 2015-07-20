package sl.ide;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author 
 */
class WatchesVariablesTableModel extends DefaultTableModel {

    public static WatchesVariablesTableModel EMPTY =
            new WatchesVariablesTableModel(new Object[][]{});

    public WatchesVariablesTableModel(Object[][] data) {
        super(data, columnNames);
    }
    private static String columnNames[] = {
        "Имя", "Тип", "Значение"
    };
    private Class[] types = new Class[]{
        java.lang.String.class, java.lang.String.class, java.lang.String.class
    };
    private boolean[] canEdit = new boolean[]{
        false, false, false
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
