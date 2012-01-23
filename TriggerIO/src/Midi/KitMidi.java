/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Midi;

import Base.Input;
import Base.Kit;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;
import org.w3c.dom.Element;

/**
 *
 * @author DrumTrigger
 */
public class KitMidi extends Kit {

    public static final int KITMESSAGELENGTH = 77;
    public static final int KITNUMBERPOSITION = 9;
    private static final int PROGRAMCHANGEPOSITION1 = 74;
    private static final int PROGRAMCHANGEPOSITION2 = 75;
    private static final byte[] HEADER = {
        0x00, 0x00, 0x0E, 0x2C, 0x0D, 0x00, 0x00, 0x46, 0x01};

    private static boolean Validate(byte[] data) {
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
    public KitMidi() {
        super();
    }

    public KitMidi(Element element) {
        super(element);
    }

    //----------------------------------------------
    public KitMidi(byte[] data) throws UserException {
        Common.logger.finer("begin");

        if (Validate(data)) {
            Common.logger.finer("validated OK");

            setKitNumber(Common.unsignedByteToInt(data[KITNUMBERPOSITION]));
            setKitName("Kit-" + Integer.toString(getKitNumber()));
            setProgramChange(Common.unsignedByteToInt(data[PROGRAMCHANGEPOSITION1]) == 0 ? Common.unsignedByteToInt(data[PROGRAMCHANGEPOSITION2]) : NOPROGRAMCHANGE);

            this.inputs = new ArrayList<Input>();

            for (int inputNumber = 0; inputNumber < DeviceMidi.COUNTINPUT; inputNumber++) {
                this.inputs.add(new Input(
                        inputNumber,
                        Common.unsignedByteToInt(data[(inputNumber * 3) + 10]),
                        Common.unsignedByteToInt(data[(inputNumber * 3) + 11])));
            }
        } else {
            Common.logger.log(Level.SEVERE, "Failed Validation Check <{0}>", Common.printMessage(data));
            throw new UserException("Kit validation failed");
        }
    }

    //----------------------------------------------
    public SysexMessage getSysexMessage() throws InvalidMidiDataException {
        SysexMessage sysexMessage = new SysexMessage();
        sysexMessage.setMessage(0xF0, getByteMessage(), KITMESSAGELENGTH);

        return sysexMessage;
    }

    //----------------------------------------------
    public byte[] getByteMessage() {
        int[] intMidiMessage = new int[KITMESSAGELENGTH];
        boolean xFlap = (this.getKitNumber() != 20);
        int offset1, offset2;

        if (this.getProgramChange() == NOPROGRAMCHANGE) {
            Common.logger.finest("NOPROGRAMCHANGE");
            offset1 = 1;
            offset2 = 127;
        } else {
            Common.logger.finest("program change set");
            offset1 = 0;
            offset2 = this.getProgramChange();
        }

        intMidiMessage[0] = HEADER[0];
        intMidiMessage[1] = HEADER[1];
        intMidiMessage[2] = HEADER[2];
        intMidiMessage[3] = HEADER[3];
        intMidiMessage[4] = HEADER[4];
        intMidiMessage[5] = HEADER[5];
        intMidiMessage[6] = HEADER[6];
        intMidiMessage[7] = HEADER[7];
        intMidiMessage[8] = HEADER[8];
        intMidiMessage[9] = this.getKitNumber();

        intMidiMessage[10] = this.inputs.get(0).getChannel();
        intMidiMessage[11] = this.inputs.get(0).getNote();
        intMidiMessage[12] = xFlap ? 3 : 0;
        intMidiMessage[13] = this.inputs.get(1).getChannel();
        intMidiMessage[14] = this.inputs.get(1).getNote();
        intMidiMessage[15] = xFlap ? 3 : 6;
        intMidiMessage[16] = this.inputs.get(2).getChannel();
        intMidiMessage[17] = this.inputs.get(2).getNote();
        intMidiMessage[18] = xFlap ? 3 : 13;
        intMidiMessage[19] = this.inputs.get(3).getChannel();
        intMidiMessage[20] = this.inputs.get(3).getNote();
        intMidiMessage[21] = xFlap ? 3 : 19;
        intMidiMessage[22] = this.inputs.get(4).getChannel();
        intMidiMessage[23] = this.inputs.get(4).getNote();
        intMidiMessage[24] = xFlap ? 3 : 26;
        intMidiMessage[25] = this.inputs.get(5).getChannel();
        intMidiMessage[26] = this.inputs.get(5).getNote();
        intMidiMessage[27] = xFlap ? 3 : 33;
        intMidiMessage[28] = this.inputs.get(6).getChannel();
        intMidiMessage[29] = this.inputs.get(6).getNote();
        intMidiMessage[30] = xFlap ? 3 : 39;
        intMidiMessage[31] = this.inputs.get(7).getChannel();
        intMidiMessage[32] = this.inputs.get(7).getNote();
        intMidiMessage[33] = xFlap ? 3 : 46;
        intMidiMessage[34] = this.inputs.get(8).getChannel();
        intMidiMessage[35] = this.inputs.get(8).getNote();
        intMidiMessage[36] = xFlap ? 3 : 53;
        intMidiMessage[37] = this.inputs.get(9).getChannel();
        intMidiMessage[38] = this.inputs.get(9).getNote();
        intMidiMessage[39] = xFlap ? 3 : 59;
        intMidiMessage[40] = this.inputs.get(10).getChannel();
        intMidiMessage[41] = this.inputs.get(10).getNote();
        intMidiMessage[42] = xFlap ? 3 : 66;
        intMidiMessage[43] = this.inputs.get(11).getChannel();
        intMidiMessage[44] = this.inputs.get(11).getNote();
        intMidiMessage[45] = xFlap ? 3 : 72;
        intMidiMessage[46] = this.inputs.get(12).getChannel();
        intMidiMessage[47] = this.inputs.get(12).getNote();
        intMidiMessage[48] = xFlap ? 3 : 79;
        intMidiMessage[49] = this.inputs.get(13).getChannel();
        intMidiMessage[50] = this.inputs.get(13).getNote();
        intMidiMessage[51] = xFlap ? 3 : 86;
        intMidiMessage[52] = this.inputs.get(14).getChannel();
        intMidiMessage[53] = this.inputs.get(14).getNote();
        intMidiMessage[54] = xFlap ? 3 : 92;
        intMidiMessage[55] = this.inputs.get(15).getChannel();
        intMidiMessage[56] = this.inputs.get(15).getNote();
        intMidiMessage[57] = xFlap ? 3 : 99;
        intMidiMessage[58] = this.inputs.get(16).getChannel();
        intMidiMessage[59] = this.inputs.get(16).getNote();
        intMidiMessage[60] = xFlap ? 3 : 106;
        intMidiMessage[61] = this.inputs.get(17).getChannel();
        intMidiMessage[62] = this.inputs.get(17).getNote();
        intMidiMessage[63] = xFlap ? 3 : 112;
        intMidiMessage[64] = this.inputs.get(18).getChannel();
        intMidiMessage[65] = this.inputs.get(18).getNote();
        intMidiMessage[66] = xFlap ? 3 : 119;
        intMidiMessage[67] = this.inputs.get(19).getChannel();
        intMidiMessage[68] = this.inputs.get(19).getNote();
        intMidiMessage[69] = xFlap ? 3 : 126;
        intMidiMessage[70] = this.inputs.get(20).getChannel();
        intMidiMessage[71] = this.inputs.get(20).getNote();
        intMidiMessage[72] = xFlap ? 3 : 127;
        intMidiMessage[73] = 42;
        intMidiMessage[74] = offset1;
        intMidiMessage[75] = offset2;
        intMidiMessage[76] = 0xF7;

        Common.logger.log(Level.FINEST, "intMidiMessage=<{0}>", intMidiMessage);

        return Common.intToByte(intMidiMessage);
    }
}
