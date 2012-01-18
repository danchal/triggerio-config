/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Base;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 *
 * @author dan
 */
public class Kit {
    public static final int NOPROGRAMCHANGE                 = -1;
    public static final String NOPROGRAMCHANGE_VAL   = "---";
    
    public static final Map<Integer, String> lovProgramChange = new LinkedHashMap<Integer, String>();

    private String kitName;
    private int kitNumber;
    private int programChange;
    public List<MidiChannel> triggerMidiChannel;
    public List<MidiNote> triggerMidiNote;    
    
    static {
         // build LOV program change
        lovProgramChange.put(Kit.NOPROGRAMCHANGE, Kit.NOPROGRAMCHANGE_VAL);

        for (int i = 0; i <= 127; i++) {
            lovProgramChange.put(i, Integer.toString(i));
        }
    }
    
    protected  Kit(){
    }
    
    //----------------------------------------------
    public Kit(int kitNumber, String kitName, int programChange,
            List<MidiChannel> triggerMidiChannel,
            List<MidiNote> triggerMidiNote) {
    }    
    
   //----------------------------------------------
    public final String getKitName() {
        return this.kitName;
    }

    //----------------------------------------------
    public final int getKitNumber() {
        return this.kitNumber;
    }

    //----------------------------------------------
    public final int getProgramChange() {
        return this.programChange;
    }

    //----------------------------------------------
    protected void setKitNumber(int kitNumber) {
        this.kitNumber = kitNumber;
        Common.logger.log(Level.FINEST, "kitNumber<{0}>", this.kitNumber);
    }

    //----------------------------------------------
    public final void setKitName(String kitName) {
        this.kitName = kitName;
        Common.logger.log(Level.FINEST, "kitName<{0}>", this.kitName);
    }

    //----------------------------------------------
    public final void setProgramChange(int programChange) {
        if ( lovProgramChange.containsKey(programChange) ){
            this.programChange = programChange;
            Common.logger.log(Level.FINEST, "programChange<{0}>", this.programChange);
        }
        else{
            Common.logger.log(Level.WARNING, "Unrecognised programChange<{0}>", programChange);
        }
    }    
    
}
