package cfh.jfalse.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import cfh.jfalse.Environment;
import cfh.jfalse.SingleVariables;
import cfh.jfalse.stack.StackObject;

public class TableVariables extends SingleVariables implements PropertyChangeListener {

    public static final String ADDR_LABEL = "#";
    public static final String VALUE_LABEL = "value";
    
    private final VariablesModel model;
    
    private boolean update = true;
    
    public TableVariables() {
        model = new VariablesModel();
    }
    
    @Override
    public void assign(char address, StackObject value) {
        super.assign(address, value);
        model.fireTableCellUpdated(index(address), 1);
    }
    
    public TableModel getModel() {
        return model;
    }
    
    public void clear() {
        Arrays.fill(memory, null);
        model.fireTableDataChanged();
    }
    
    private void setUpdate(boolean update) {
        if (update != this.update) {
            this.update = update;
            model.fireTableDataChanged();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String name = evt.getPropertyName();
        if (name.equals(Environment.PROP_STATUS)) {
            setUpdate(evt.getNewValue() != Environment.Status.RUNNING);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    
    private class VariablesModel extends AbstractTableModel {

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public int getRowCount() {
            return memory.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0: return " " + (char)('a' + rowIndex);
                case 1: return memory[rowIndex];
                default: return " ? ";
            }
        }

        @Override
        public String getColumnName(int column) {
            switch(column) {
                case 0: return ADDR_LABEL;
                case 1: return VALUE_LABEL;
                default: return " ? ";
            }
        }

        @Override
        public void fireTableCellUpdated(int row, int column) {
            if (update)
                super.fireTableCellUpdated(row, column);
        }

        @Override
        public void fireTableDataChanged() {
            if (update)
                super.fireTableDataChanged();
        }
    }
}
