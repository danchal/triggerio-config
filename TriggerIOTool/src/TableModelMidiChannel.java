/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import Base.MidiChannel;
import Midi.Common;
import Midi.Device;
import Midi.Kit;
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
        return Common.getKey(data[row][col], MidiChannel.lovTriggerMidiChannel);
    }    

    public void load(Kit[] kits){
        for(Kit kit : kits){
            columnNames[kit.getKitNumber() + 1] = kit.getKitName();
            for(MidiChannel triggerMidiChannel : kit.triggerMidiChannel){
                Common.logger.log(Level.FINEST,"kitNumber = <{0}" + ">"
                        + ", inputNumber = <{1}" + ">" + ", value = <{2}>" , new Object[]{kit.getKitNumber(), triggerMidiChannel.getTriggerInputNumber(), triggerMidiChannel.getTriggerMidiChannel()});
                data[triggerMidiChannel.getTriggerInputNumber()][0] = "Input-" + triggerMidiChannel.getTriggerInputNumber();
                data[triggerMidiChannel.getTriggerInputNumber()][kit.getKitNumber() + 1] = MidiChannel.lovTriggerMidiChannel.get(triggerMidiChannel.getTriggerMidiChannel());
            }
        }
    }

    public Kit[] synchronise(Kit[] kitsIn){
        Kit[] kitsOut = kitsIn;

        for(Kit kit : kitsIn){
            for(MidiChannel triggerMidiChannel : kit.triggerMidiChannel){
                Common.logger.log(Level.FINEST,"kitNumber = <{0}" + ">"
                        + ", inputNumber = <{1}" + ">" + ", value = <{2}>" , new Object[]{kit.getKitNumber(), triggerMidiChannel.getTriggerInputNumber(), triggerMidiChannel.getTriggerMidiChannel()});
                int inputNumber = triggerMidiChannel.getTriggerInputNumber();
                kitsOut[kit.getKitNumber()].triggerMidiChannel.get(inputNumber).setTriggerMidiChannel(getRealValueAt(inputNumber, kit.getKitNumber()+1));
            }
        }
        return kitsOut;
    }
}