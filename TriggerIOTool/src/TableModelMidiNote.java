/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import Base.MidiNote;
import Midi.Common;
import Midi.Device;
import Midi.Kit;
import java.util.logging.Level;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author DrumTrigger
 */
public class TableModelMidiNote extends AbstractTableModel{

    private String[] columnNames;
    private Object[][] data;

    public TableModelMidiNote() {
        columnNames = new String[Device.COUNTKIT +1];
        data        = new Object[Device.COUNTINPUT][Device.COUNTKIT +1];

        columnNames[0] = "Input";
        for(int kitNumber=0; kitNumber<Device.COUNTKIT; kitNumber++){
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
        return Common.getKey(data[row][col], MidiNote.lovTriggerMidiNote);
    }

    public void load(Kit[] kits){
        for(Kit kit : kits){
            columnNames[kit.getKitNumber() + 1] = kit.getKitName();
            for(MidiNote triggerMidiNote : kit.triggerMidiNote){
                Common.logger.log(Level.FINEST,"kitNumber = <{0}" + ">"
                        + ", inputNumber = <{1}" + ">" + ", value = <{2}>" , 
                        new Object[]{kit.getKitNumber(), triggerMidiNote.getTriggerInputNumber(), triggerMidiNote.getTriggerMidiNote()});
                data[triggerMidiNote.getTriggerInputNumber()][0] = "Input-" + triggerMidiNote.getTriggerInputNumber();
                data[triggerMidiNote.getTriggerInputNumber()][kit.getKitNumber() + 1] = MidiNote.lovTriggerMidiNote.get(triggerMidiNote.getTriggerMidiNote());
            }
        }
    }

    public Kit[] synchronise(Kit[] kitsIn){
        Kit[] kitsOut = kitsIn;
        for(Kit kit : kitsIn){
            for(MidiNote triggerMidiNote : kit.triggerMidiNote){
                Common.logger.log(Level.FINEST,"kitNumber = <{0}" + ">"
                        + ", inputNumber = <{1}" + ">" + ", value = <{2}>" , new Object[]{kit.getKitNumber(), triggerMidiNote.getTriggerInputNumber(), triggerMidiNote.getTriggerMidiNote()});
                int inputNumber = triggerMidiNote.getTriggerInputNumber();
                kitsOut[kit.getKitNumber()].triggerMidiNote.get(inputNumber).setTriggerMidiNote(getRealValueAt(inputNumber, kit.getKitNumber()+1));
            }
        }
        return kitsOut;
    }
}