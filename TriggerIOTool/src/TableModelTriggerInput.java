/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import Midi.Common;
import Midi.Device;
import Midi.Input;
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
        data        = new Object[Device.COUNTINPUT][this.columnNames.length];
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

    public Input getTriggerInput(int inputNumber){
        Common.logger.finest(String.valueOf(inputNumber));

        return new Input(
                inputNumber,
                data[inputNumber][0].toString(),
                Common.getKey(data[inputNumber][1],Input.lovGain),
                Common.getKey(data[inputNumber][2],Input.lovVelocityCurve),
                Common.getKey(data[inputNumber][3],Input.lovThreshold),
                Common.getKey(data[inputNumber][4],Input.lovXTalk),
                Common.getKey(data[inputNumber][5],Input.lovRetrigger),
                Common.getKey(data[inputNumber][6],Input.lovTriggerType));
    }
    
    public void load(Input[] triggerInputs){
        for(Input triggerInput : triggerInputs){
            Common.logger.log(Level.FINEST,"Inputnumber = <{0}" + ">" + ", name = <{1}"
                        + ">" + ", gain = <{2}" + ">"
                        + ", velo = <{3}" + ">" + ", threshold = <{4}"
                        + ">" + ", xtalk = <{5}" + ">"
                        + ", retrigger = <{6}" + ">" + ", trigger type = <{7}>", new Object[]{triggerInput.getTriggerInputNumber(), triggerInput.getTriggerInputName(), triggerInput.getGain(), triggerInput.getVelocityCurve(), triggerInput.getThreshold(), triggerInput.getXTalk(), triggerInput.getRetrigger(), triggerInput.getTriggerType()});
            data[triggerInput.getTriggerInputNumber()][0] = triggerInput.getTriggerInputName();
            data[triggerInput.getTriggerInputNumber()][1] = Input.lovGain.get(triggerInput.getGain());
            data[triggerInput.getTriggerInputNumber()][2] = Input.lovVelocityCurve.get(triggerInput.getVelocityCurve());
            data[triggerInput.getTriggerInputNumber()][3] = Input.lovThreshold.get(triggerInput.getThreshold());
            data[triggerInput.getTriggerInputNumber()][4] = Input.lovXTalk.get(triggerInput.getXTalk());
            data[triggerInput.getTriggerInputNumber()][5] = Input.lovRetrigger.get(triggerInput.getRetrigger());
            data[triggerInput.getTriggerInputNumber()][6] = Input.lovTriggerType.get(triggerInput.getTriggerType());
        }
    }

    public Input[] synchronise(Input[] triggerInputsIn){
        Input[] triggerInputsOut = triggerInputsIn;
        
        for(Input triggerInput : triggerInputsIn){

            Common.logger.log(
                           Level.FINEST,"Inputnumber = <{0}" + ">" + ", name = <{1}"
                        + ">" + ", gain = <{2}" + ">"
                        + ", velo = <{3}" + ">" + ", threshold = <{4}"
                        + ">" + ", xtalk = <{5}" + ">"
                        + ", retrigger = <{6}" + ">" + ", trigger type = <{7}>", new Object[]{triggerInput.getTriggerInputNumber(), triggerInput.getTriggerInputName(), triggerInput.getGain(), triggerInput.getVelocityCurve(), triggerInput.getThreshold(), triggerInput.getXTalk(), triggerInput.getRetrigger(), triggerInput.getTriggerType()});

            int inputNumber = triggerInput.getTriggerInputNumber();
            triggerInputsOut[inputNumber] = getTriggerInput(inputNumber);
        }
        return triggerInputsOut;
    }
}