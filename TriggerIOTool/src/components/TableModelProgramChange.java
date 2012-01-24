package components;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import TriggerIO.Midi.Common;
import TriggerIO.Midi.DeviceMidi;
import TriggerIO.Midi.KitMidi;
import java.util.List;
import java.util.logging.Level;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author DrumTrigger
 */
public class TableModelProgramChange extends AbstractTableModel{
    private String[] columnNames;
    private Object[][] data;

    public TableModelProgramChange() {
        columnNames = new String[DeviceMidi.COUNTKIT];
        data        = new Object[1][DeviceMidi.COUNTKIT];

        for(int i=0; i<columnNames.length; i++){
            this.columnNames[i] = "Kit-" + i;
        }
    }

    public int getRowCount() {
        return data.length;
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
        return true;
    }
    @Override
    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }

    public void load(List <KitMidi> kits){
        for(KitMidi kit : kits){
            Common.logger.log(Level.FINEST, "kitNumber = <{0}>, value = <{1}>", new Object[]{kit.getKitNumber(), kit.getProgramChange()});
            data[0][kit.getKitNumber()] = KitMidi.lovProgramChange.get(kit.getProgramChange());
        }
    }

    public int getRealValueAt(int row, int col){
        return Common.getKey(data[row][col], KitMidi.lovProgramChange);
    }
}