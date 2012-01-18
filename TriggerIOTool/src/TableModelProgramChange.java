/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import Midi.Common;
import Midi.Device;
import Midi.Kit;
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
        columnNames = new String[Device.COUNTKIT];
        data        = new Object[1][Device.COUNTKIT];

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

    public void load(Kit[] kits){
        for(Kit kit : kits){
            Common.logger.log(Level.FINEST, "kitNumber = <{0}>, value = <{1}>", new Object[]{kit.getKitNumber(), kit.getProgramChange()});
            data[0][kit.getKitNumber()] = Kit.lovProgramChange.get(kit.getProgramChange());
        }
    }

    public int getRealValueAt(int row, int col){
        return Common.getKey(data[row][col], Kit.lovProgramChange);
    }
    
    public Kit[] synchronise(Kit[] kits){
        Kit[] kitsOut = kits;
        for(Kit kit : kits){
            Common.logger.log(Level.FINEST,"KitNumber = <{0}" + ">" + ", value = <{1}>", new Object[]{kit.getKitNumber(), kit.getProgramChange()});

            kitsOut[kit.getKitNumber()].setProgramChange(getRealValueAt(0, kit.getKitNumber()));
        }

        return kitsOut;
    }

}