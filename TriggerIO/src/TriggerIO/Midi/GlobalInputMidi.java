/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TriggerIO.Midi;

import TriggerIO.Base.GlobalInput;
import java.util.logging.Level;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;
import org.w3c.dom.Element;

/**
 *
 * @author DrumTrigger
 */
public class GlobalInputMidi extends GlobalInput {

    public static final int INPUTMESSAGELENGTH = 17;
    public static final int INPUTNUMBERPOSITION = 9;
    public static Exception validationFailed;
    private static final byte[] HEADER = {
        0x00, 0x00, 0x0E, 0x2C, 0x0D, 0x00, 0x00, 0x0A, 0x02};

    public GlobalInputMidi() {
    }

    public GlobalInputMidi(Element element) {
        super(element);
    }

    //----------------------------------------------
    public GlobalInputMidi(int triggerInputNumber, String triggerInputName,
            int gain, int velocityCurve, int threshold,
            int xTalk, int retrigger, int triggerType) {

        super(triggerInputNumber, triggerInputName,
                gain, velocityCurve, threshold,
                xTalk, retrigger, triggerType);
    }

    //----------------------------------------------
    public GlobalInputMidi(byte[] data) throws UserException {
        Common.logger.finer("begin");

        if (Validate(data)) {
            Common.logger.finer("validated OK");

            int inputNumber = Common.unsignedByteToInt(data[INPUTNUMBERPOSITION]);
            setInputNumber(inputNumber);
            setInputName("Input-" + String.valueOf((inputNumber / 2) + 1) + (inputNumber % 2 == 0 ? "T" : "R"));

            setGain(Common.unsignedByteToInt(data[10]));
            setVelocityCurve(Common.unsignedByteToInt(data[11]));
            setThreshold(Common.unsignedByteToInt(data[12]));
            setXTalk(Common.unsignedByteToInt(data[13]));
            setRetrigger(Common.unsignedByteToInt(data[14]));
            setTriggerType(Common.unsignedByteToInt(data[15]));
        } else {
            Common.logger.log(Level.SEVERE, "Failed Validation Check <{0}>", Common.printMessage(data));
            throw new UserException("Kit validation failed");
        }
    }

    //---------------------------------------------------------------------
    public static boolean Validate(byte[] data) {
        return ((data[0] == HEADER[0])
                && (data[1] == HEADER[1])
                && (data[2] == HEADER[2])
                && (data[3] == HEADER[3])
                && (data[4] == HEADER[4])
                && (data[5] == HEADER[5])
                && (data[6] == HEADER[6])
                && (data[7] == HEADER[7])
                && (data[8] == HEADER[8]));
    }

    //----------------------------------------------
    public SysexMessage getSysexMessage() throws InvalidMidiDataException {
        SysexMessage sysexMessage = new SysexMessage();
        sysexMessage.setMessage(0xF0, getByteMessage(), INPUTMESSAGELENGTH);

        return sysexMessage;
    }

    //----------------------------------------------
    public byte[] getByteMessage() {
        Common.logger.finest("begin");

        int[] intMidiMessage = new int[INPUTMESSAGELENGTH];
        intMidiMessage[0] = HEADER[0];
        intMidiMessage[1] = HEADER[1];
        intMidiMessage[2] = HEADER[2];
        intMidiMessage[3] = HEADER[3];
        intMidiMessage[4] = HEADER[4];
        intMidiMessage[5] = HEADER[5];
        intMidiMessage[6] = HEADER[6];
        intMidiMessage[7] = HEADER[7];
        intMidiMessage[8] = HEADER[8];
        intMidiMessage[9] = this.getTriggerInputNumber();
        intMidiMessage[10] = this.getGain();
        intMidiMessage[11] = this.getVelocityCurve();
        intMidiMessage[12] = this.getThreshold();
        intMidiMessage[13] = this.getXTalk();
        intMidiMessage[14] = this.getRetrigger();
        intMidiMessage[15] = this.getTriggerType();
        intMidiMessage[16] = 0xF7;

        Common.logger.finest("end");
        return Common.intToByte(intMidiMessage);
    }
}
