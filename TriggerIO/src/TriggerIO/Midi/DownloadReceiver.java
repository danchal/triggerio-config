/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TriggerIO.Midi;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.SysexMessage;

/**
 *
 * @author DrumTrigger
 */
public class DownloadReceiver implements Receiver {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public List <KitMidi> kits = new ArrayList<KitMidi>();
    public List <GlobalInputMidi> triggerInputs = new ArrayList<GlobalInputMidi>();
    public List <byte[]> midiMessages = new ArrayList<byte[]>();

    //---------------------------------------------------------------------
    public void send(MidiMessage message, long timeStamp) {
        if (message.getStatus() == 0xF0 || message.getStatus() == 0xF7) {
            SysexMessage sysexMessage = (SysexMessage) message;
            midiMessages.add(sysexMessage.getData());
        } else {
            Common.logger.log(Level.INFO, "Not a sysex message? <{0}>", message.getStatus());
        }
    }

    //---------------------------------------------------------------------
    public void close() {
        Common.logger.log(Level.FINE, "Message count=<{0}>", midiMessages.size());

        for (byte[] data : midiMessages) {

            Common.logger.finer(Common.printMessage(data));

            try {
                switch (data.length) {
                    case KitMidi.KITMESSAGELENGTH:
                        kits.add(Common.unsignedByteToInt(data[KitMidi.KITNUMBERPOSITION]), new KitMidi(data));
                        break;
                    case GlobalInputMidi.INPUTMESSAGELENGTH:
                        triggerInputs.add(Common.unsignedByteToInt(data[KitMidi.KITNUMBERPOSITION]), new GlobalInputMidi(data));
                        break;
                    default:
                        Common.logger.severe("Unrecognised message length");
                        break;
                }
            } catch (Exception ex) {
                Common.logger.severe(ex.getLocalizedMessage());
            }
        }
        Common.logger.fine("End");
    }
}
