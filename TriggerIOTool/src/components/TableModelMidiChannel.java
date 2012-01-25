package components;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import TriggerIO.Base.Input;
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
public class TableModelMidiChannel extends AbstractTableModel{

    private String[] columnNames;
    private Object[][] data;

    public TableModelMidiChannel() {
        columnNames = new String[DeviceMidi.COUNTKIT +1];
        data        = new Object[DeviceMidi.COUNTINPUT][DeviceMidi.COUNTKIT +1];

        columnNames[0] = "Input";
        for(int kitNumber=0; kitNumber<DeviceMidi.COUNTKIT; kitNumber++){
            columnNames[kitNumber+1] = "Kit-" + kitNumber;
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
        if (col ==0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }

    public int getRealValueAt(int row, int col){
        return Common.getKey(data[row][col], Input.lovChannel);
    }

    public void load(List <KitMidi> kits){
        for(KitMidi kit : kits){
            columnNames[kit.getKitNumber() + 1] = kit.getKitName();

            for(Input input : kit.inputs){
                Common.logger.log(Level.FINEST,"kitNumber = <{0}" + ">"
                        + ", inputNumber = <{1}" + ">" + ", value = <{2}>" , new Object[]{kit.getKitNumber(), input.getinputNumber(), input.getChannel()});
                data[input.getinputNumber()][0] = "Input-" + input.getinputNumber();
                data[input.getinputNumber()][kit.getKitNumber() + 1] = Input.lovChannel.get(input.getChannel());
            }
        }
    }
}