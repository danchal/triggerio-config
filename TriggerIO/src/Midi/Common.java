package Midi;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.sound.midi.*;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author DrumTrigger
 */
public class Common extends Base.Common{

    //----------------------------------------------
    public Common() {
    }

    public static String printMessage(byte[] message){
        String msg = "length = <" + message.length + ">";
        for (int i = 0; i < message.length; i++) {
            msg = msg + ", data[" + i + "] = <" + unsignedByteToInt(message[i]) + ">";
        }
        return msg;
    }

    //----------------------------------------------
    public static byte[] intToByte(int[] intMidiMessage) {
        Common.logger.log(Level.FINEST, "int length = <{0}>", intMidiMessage.length);

        byte[] bytMidiMessage = new byte[intMidiMessage.length];
        for (int i = 0; i < intMidiMessage.length; i++) {
            bytMidiMessage[i] = (byte) intMidiMessage[i];
        }

        Common.logger.log(Level.FINEST, "byt length = <{0}>", bytMidiMessage.length);
        return bytMidiMessage;
    }

    //----------------------------------------------
    public static int unsignedByteToInt(byte b) {
        return (int) (b & 0xFF);
    }

    //----------------------------------------------
    public static List<MidiDevice.Info> getReceivingDevices() {
        return getDevices(false);
    }
    //----------------------------------------------

    public static List<MidiDevice.Info> getTransmittingDevices() {
        return getDevices(true);
    }

    //----------------------------------------------
    private static List<MidiDevice.Info> getDevices(boolean typeTansmitter) {
        Common.logger.log(Level.FINE, "typeTansmitter=<{0}>", String.valueOf(typeTansmitter));

        List<MidiDevice.Info> deviceInfos = new ArrayList<MidiDevice.Info>();
        MidiDevice.Info[] allDevices = MidiSystem.getMidiDeviceInfo();

        for (int i = 0; i < allDevices.length; i++) {
            try {
                MidiDevice testDevice = MidiSystem.getMidiDevice(allDevices[i]);

                Common.logger.fine(
                                     testDevice.getDeviceInfo()
                        + ", " + allDevices[i].getName()
                        + ", " + allDevices[i].getDescription()
                        + ", " + allDevices[i].getVendor()
                        + ", " + allDevices[i].getVersion());

                if (  !(testDevice instanceof Sequencer || testDevice instanceof Synthesizer)
                        && (   (typeTansmitter && testDevice.getMaxTransmitters() != 0)
                                || (!typeTansmitter && testDevice.getMaxReceivers() != 0) )) {
                    deviceInfos.add(allDevices[i]);
                }
                else{
                    Common.logger.fine("Device excluded");
                }
            } catch (MidiUnavailableException ex) {
                Common.logger.log(Level.WARNING, "Midi Device Unavailable: {0}", allDevices[i].getDescription());
            }
        }

        Common.logger.log(Level.FINE, "Devices found = <{0}>", deviceInfos.size());
        return deviceInfos;
    }
}
