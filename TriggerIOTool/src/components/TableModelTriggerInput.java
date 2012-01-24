package components;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import TriggerIO.Midi.Common;
import TriggerIO.Midi.DeviceMidi;
import TriggerIO.Midi.GlobalInputMidi;
import java.util.List;
import java.util.logging.Level;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author DrumTrigger
 */
public class TableModelTriggerInput extends AbstractTableModel{

    private String[] columnNames = {"Input", "Gain", "Velocity Curve", "Threshold", "X-Talk", "Retrigger", "Trigger Type"};
    private Object[][] data;

    public TableModelTriggerInput() {
        data        = new Object[DeviceMidi.COUNTINPUT][this.columnNames.length];
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
        boolean editable = false;

        switch(col){
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                editable = true;
                break;
            case 6:
                if(row %2 ==0){
                 editable = true;
                }
                break;
        }
        return editable;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);

        if(col == 6 && row%2 == 0 && row != 20){
            data[row+1][col] = value;
            fireTableCellUpdated(row+1, col);
        }
    }

    public GlobalInputMidi getTriggerInput(int inputNumber){
        Common.logger.finest(String.valueOf(inputNumber));

        return new GlobalInputMidi(
                inputNumber,
                data[inputNumber][0].toString(),
                Common.getKey(data[inputNumber][1],GlobalInputMidi.lovGain),
                Common.getKey(data[inputNumber][2],GlobalInputMidi.lovVelocity),
                Common.getKey(data[inputNumber][3],GlobalInputMidi.lovThreshold),
                Common.getKey(data[inputNumber][4],GlobalInputMidi.lovXTalk),
                Common.getKey(data[inputNumber][5],GlobalInputMidi.lovRetrigger),
                Common.getKey(data[inputNumber][6],GlobalInputMidi.lovType));
    }

    public void load(List <GlobalInputMidi> triggerInputs){
        for(GlobalInputMidi triggerInput : triggerInputs){
            Common.logger.log(Level.FINEST,"Inputnumber = <{0}" + ">" + ", name = <{1}"
                        + ">" + ", gain = <{2}" + ">"
                        + ", velo = <{3}" + ">" + ", threshold = <{4}"
                        + ">" + ", xtalk = <{5}" + ">"
                        + ", retrigger = <{6}" + ">" + ", trigger type = <{7}>", new Object[]{triggerInput.getTriggerInputNumber(), triggerInput.getTriggerInputName(), triggerInput.getGain(), triggerInput.getVelocityCurve(), triggerInput.getThreshold(), triggerInput.getXTalk(), triggerInput.getRetrigger(), triggerInput.getTriggerType()});
            data[triggerInput.getTriggerInputNumber()][0] = triggerInput.getTriggerInputName();
            data[triggerInput.getTriggerInputNumber()][1] = GlobalInputMidi.lovGain.get(triggerInput.getGain());
            data[triggerInput.getTriggerInputNumber()][2] = GlobalInputMidi.lovVelocity.get(triggerInput.getVelocityCurve());
            data[triggerInput.getTriggerInputNumber()][3] = GlobalInputMidi.lovThreshold.get(triggerInput.getThreshold());
            data[triggerInput.getTriggerInputNumber()][4] = GlobalInputMidi.lovXTalk.get(triggerInput.getXTalk());
            data[triggerInput.getTriggerInputNumber()][5] = GlobalInputMidi.lovRetrigger.get(triggerInput.getRetrigger());
            data[triggerInput.getTriggerInputNumber()][6] = GlobalInputMidi.lovType.get(triggerInput.getTriggerType());
        }
    }
}