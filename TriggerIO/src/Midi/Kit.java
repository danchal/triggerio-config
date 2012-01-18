/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Midi;

import Base.MidiChannel;
import Base.MidiNote;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;

/**
 *
 * @author DrumTrigger
 */
public class Kit extends Base.Kit {

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
    public Kit(byte[] data) throws UserException {   
        super();
        
        Common.logger.finer("begin");

        if (Validate(data)) {
            Common.logger.finer("validated OK");

            triggerMidiChannel = new ArrayList<MidiChannel>();
            triggerMidiNote = new ArrayList<MidiNote>();

            setKitNumber(Common.unsignedByteToInt(data[KITNUMBERPOSITION]));
            setKitName("Kit-" + Integer.toString(getKitNumber()));
            setProgramChange(Common.unsignedByteToInt(data[PROGRAMCHANGEPOSITION1]) == 0 ? Common.unsignedByteToInt(data[PROGRAMCHANGEPOSITION2]) : NOPROGRAMCHANGE);

            for (int inputNumber = 0; inputNumber < Device.COUNTINPUT; inputNumber++) {
                triggerMidiChannel.add(new MidiChannel(inputNumber, Common.unsignedByteToInt(data[(inputNumber * 3) + 10])));
                triggerMidiNote.add(new MidiNote(inputNumber, Common.unsignedByteToInt(data[(inputNumber * 3) + 11])));
            }
        } else {
            Common.logger.log(Level.SEVERE, "Failed Validation Check <{0}>", Common.printMessage(data));
            throw new UserException("Kit validation failed");
        }
    }

    //----------------------------------------------
    public final SysexMessage getSysexMessage() throws InvalidMidiDataException {
        SysexMessage sysexMessage = new SysexMessage();
        sysexMessage.setMessage(0xF0, getByteMessage(), KITMESSAGELENGTH);

        return sysexMessage;
    }

    //----------------------------------------------
    public final byte[] getByteMessage() {

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

        intMidiMessage[10] = this.triggerMidiChannel.get(0).getTriggerMidiChannel();
        intMidiMessage[11] = this.triggerMidiNote.get(0).getTriggerMidiNote();
        intMidiMessage[12] = xFlap ? 3 : 0;
        intMidiMessage[13] = this.triggerMidiChannel.get(1).getTriggerMidiChannel();
        intMidiMessage[14] = this.triggerMidiNote.get(1).getTriggerMidiNote();
        intMidiMessage[15] = xFlap ? 3 : 6;
        intMidiMessage[16] = this.triggerMidiChannel.get(2).getTriggerMidiChannel();
        intMidiMessage[17] = this.triggerMidiNote.get(2).getTriggerMidiNote();
        intMidiMessage[18] = xFlap ? 3 : 13;
        intMidiMessage[19] = this.triggerMidiChannel.get(3).getTriggerMidiChannel();
        intMidiMessage[20] = this.triggerMidiNote.get(3).getTriggerMidiNote();
        intMidiMessage[21] = xFlap ? 3 : 19;
        intMidiMessage[22] = this.triggerMidiChannel.get(4).getTriggerMidiChannel();
        intMidiMessage[23] = this.triggerMidiNote.get(4).getTriggerMidiNote();
        intMidiMessage[24] = xFlap ? 3 : 26;
        intMidiMessage[25] = this.triggerMidiChannel.get(5).getTriggerMidiChannel();
        intMidiMessage[26] = this.triggerMidiNote.get(5).getTriggerMidiNote();
        intMidiMessage[27] = xFlap ? 3 : 33;
        intMidiMessage[28] = this.triggerMidiChannel.get(6).getTriggerMidiChannel();
        intMidiMessage[29] = this.triggerMidiNote.get(6).getTriggerMidiNote();
        intMidiMessage[30] = xFlap ? 3 : 39;
        intMidiMessage[31] = this.triggerMidiChannel.get(7).getTriggerMidiChannel();
        intMidiMessage[32] = this.triggerMidiNote.get(7).getTriggerMidiNote();
        intMidiMessage[33] = xFlap ? 3 : 46;
        intMidiMessage[34] = this.triggerMidiChannel.get(8).getTriggerMidiChannel();
        intMidiMessage[35] = this.triggerMidiNote.get(8).getTriggerMidiNote();
        intMidiMessage[36] = xFlap ? 3 : 53;
        intMidiMessage[37] = this.triggerMidiChannel.get(9).getTriggerMidiChannel();
        intMidiMessage[38] = this.triggerMidiNote.get(9).getTriggerMidiNote();
        intMidiMessage[39] = xFlap ? 3 : 59;
        intMidiMessage[40] = this.triggerMidiChannel.get(10).getTriggerMidiChannel();
        intMidiMessage[41] = this.triggerMidiNote.get(10).getTriggerMidiNote();
        intMidiMessage[42] = xFlap ? 3 : 66;
        intMidiMessage[43] = this.triggerMidiChannel.get(11).getTriggerMidiChannel();
        intMidiMessage[44] = this.triggerMidiNote.get(11).getTriggerMidiNote();
        intMidiMessage[45] = xFlap ? 3 : 72;
        intMidiMessage[46] = this.triggerMidiChannel.get(12).getTriggerMidiChannel();
        intMidiMessage[47] = this.triggerMidiNote.get(12).getTriggerMidiNote();
        intMidiMessage[48] = xFlap ? 3 : 79;
        intMidiMessage[49] = this.triggerMidiChannel.get(13).getTriggerMidiChannel();
        intMidiMessage[50] = this.triggerMidiNote.get(13).getTriggerMidiNote();
        intMidiMessage[51] = xFlap ? 3 : 86;
        intMidiMessage[52] = this.triggerMidiChannel.get(14).getTriggerMidiChannel();
        intMidiMessage[53] = this.triggerMidiNote.get(14).getTriggerMidiNote();
        intMidiMessage[54] = xFlap ? 3 : 92;
        intMidiMessage[55] = this.triggerMidiChannel.get(15).getTriggerMidiChannel();
        intMidiMessage[56] = this.triggerMidiNote.get(15).getTriggerMidiNote();
        intMidiMessage[57] = xFlap ? 3 : 99;
        intMidiMessage[58] = this.triggerMidiChannel.get(16).getTriggerMidiChannel();
        intMidiMessage[59] = this.triggerMidiNote.get(16).getTriggerMidiNote();
        intMidiMessage[60] = xFlap ? 3 : 106;
        intMidiMessage[61] = this.triggerMidiChannel.get(17).getTriggerMidiChannel();
        intMidiMessage[62] = this.triggerMidiNote.get(17).getTriggerMidiNote();
        intMidiMessage[63] = xFlap ? 3 : 112;
        intMidiMessage[64] = this.triggerMidiChannel.get(18).getTriggerMidiChannel();
        intMidiMessage[65] = this.triggerMidiNote.get(18).getTriggerMidiNote();
        intMidiMessage[66] = xFlap ? 3 : 119;
        intMidiMessage[67] = this.triggerMidiChannel.get(19).getTriggerMidiChannel();
        intMidiMessage[68] = this.triggerMidiNote.get(19).getTriggerMidiNote();
        intMidiMessage[69] = xFlap ? 3 : 126;
        intMidiMessage[70] = this.triggerMidiChannel.get(20).getTriggerMidiChannel();
        intMidiMessage[71] = this.triggerMidiNote.get(20).getTriggerMidiNote();
        intMidiMessage[72] = xFlap ? 3 : 127;
        intMidiMessage[73] = 42;
        intMidiMessage[74] = offset1;
        intMidiMessage[75] = offset2;
        intMidiMessage[76] = 0xF7;

        Common.logger.finest("end");

        return Common.intToByte(intMidiMessage);
    }
}
