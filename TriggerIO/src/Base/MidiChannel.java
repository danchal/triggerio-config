/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Base;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 *
 * @author DrumTrigger
 */
public class MidiChannel {
    private int triggerInputNumber;
    private int triggerMidiChannel;

    public static final Map<Integer, String> lovTriggerMidiChannel = new LinkedHashMap<Integer, String>();

    static {
        // build LOV midi channel
        for (int i = 0; i <= 15; i++) {
            lovTriggerMidiChannel.put(i, Integer.toString(i + 1));
        }
    }

    //----------------------------------------------
    public MidiChannel(int triggerInputNumber, int triggerMidiChannel) {
        setTriggerInputNumber(triggerInputNumber);
        setTriggerMidiChannel(triggerMidiChannel);
    }

    //----------------------------------------------
    public final int getTriggerInputNumber() {
        return this.triggerInputNumber;
    }

    //----------------------------------------------
    public final int getTriggerMidiChannel() {
        return this.triggerMidiChannel;
    }

    //----------------------------------------------
    private void setTriggerInputNumber(int triggerInputNumber) {
        this.triggerInputNumber = triggerInputNumber;
        Common.logger.log(Level.FINEST, "triggerInputNumber<{0}>", this.triggerInputNumber);
    }

    //----------------------------------------------
    public final void setTriggerMidiChannel(int triggerMidiChannel) {
        this.triggerMidiChannel = triggerMidiChannel;
        Common.logger.log(Level.FINEST, "triggerMidiChannel<{0}>", this.triggerMidiChannel);
    }
}
