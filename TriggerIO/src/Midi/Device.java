/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Midi;

import java.io.*;
import java.util.logging.Level;
import javax.sound.midi.*;

/**
 *
 * @author DrumTrigger
 */
public class Device extends Base.Device{
   
    public static final int INPUTNAMELENGTH = 20;
    private static final String BEGININPUTNAMES = "BEGININPUTNAMES";

    private MidiDevice midiDevice;
    protected static Exception exBadByteLength;

    //----------------------------------------------
    public Device(File sysexFile) throws UserException, Exception {
        importSysex(sysexFile);
    }

    public Device() {
    }
       
    public MidiDevice.Info getInfo() throws Exception {
        return this.midiDevice.getDeviceInfo();
    }

    public boolean isOpen() {
        boolean isOpen = false;

        try {
             isOpen = this.midiDevice.isOpen();
        } catch (Exception e) {
        }
        Common.logger.log(Level.FINEST, "return = <{0}>", String.valueOf(isOpen));
        return isOpen;
    }

    public void close() {
        this.midiDevice.close();
        this.midiDevice = null;
        Common.logger.info("device closed");
    }

    public void open(MidiDevice.Info info) throws MidiUnavailableException {
        this.midiDevice = MidiSystem.getMidiDevice(info);
        this.midiDevice.open();
        Common.logger.log(Level.INFO, "device opened =<{0}>", this.midiDevice.getDeviceInfo().getName());
    }

    public void sendtKit(int kitNumber) throws MidiUnavailableException, InvalidMidiDataException {
        Receiver receiver = midiDevice.getReceiver();
        sendtKit(kitNumber, receiver);
    }

    public void sendtKit(int kitNumber, Receiver receiver) throws InvalidMidiDataException{
        Common.logger.log(Level.FINE, "Sending kitNumber =<{0}>", kitNumber);
        receiver.send(kits[kitNumber].getSysexMessage(), -1);
    }

    public void sendTriggerInput(int inputNumber) throws MidiUnavailableException, InvalidMidiDataException {
        Receiver receiver = midiDevice.getReceiver();
        sendTriggerInput(inputNumber, receiver);
    }

    public void sendTriggerInput(int inputNumber, Receiver receiver) throws InvalidMidiDataException {
        Common.logger.log(Level.FINE, "Sending inputNumber =<{0}>", inputNumber);
        receiver.send(triggerInputs[inputNumber].getSysexMessage(), -1);
    }

    public void send() throws InvalidMidiDataException, MidiUnavailableException {
        Common.logger.fine("begin");
        Receiver receiver = midiDevice.getReceiver();

        for (int kitNumber = 0; kitNumber < kits.length; kitNumber++){
            sendtKit(kitNumber, receiver);
        }

        for (int inputNumber = 0; inputNumber < triggerInputs.length; inputNumber++){
            sendTriggerInput(inputNumber, receiver);
        }

        receiver.close();
        Common.logger.fine("end");
    }

    private static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }

    private String getTriggerInputNames() {
        String triggerInputNames = "";
        for (int inputNumber = 0; inputNumber < triggerInputs.length; inputNumber++) {
            triggerInputNames = triggerInputNames + padRight(triggerInputs[inputNumber].getTriggerInputName(), Device.INPUTNAMELENGTH).substring(0, Device.INPUTNAMELENGTH);
        }
        return triggerInputNames;
    }

    public void export(File sysexFile) throws FileNotFoundException, IOException {
        Common.logger.fine("begin");
        DataOutputStream outputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(sysexFile)));
        
        for (int kitNumber = 0; kitNumber < kits.length; kitNumber++) {
            outputStream.write(0xF0);
            outputStream.write(kits[kitNumber].getByteMessage());
        }
        for (int inputNumber = 0; inputNumber < triggerInputs.length; inputNumber++) {
            outputStream.write(0xF0);
            outputStream.write(triggerInputs[inputNumber].getByteMessage(), 0, Input.INPUTMESSAGELENGTH);
        }
        outputStream.writeBytes(Device.BEGININPUTNAMES);
        outputStream.writeBytes(getTriggerInputNames());
        outputStream.close();
        Common.logger.fine("end");
    }


    private static byte[] readStream(DataInputStream inputStream, int length) throws IOException{
        byte[] byteStream = new byte[length];

        inputStream.read(byteStream, 0, byteStream.length);

        Common.logger.finer(Common.printMessage(byteStream));
        return byteStream;
    }

    //----------------------------------------------
    public final  void importSysex(File sysexFile) throws  UserException, FileNotFoundException, Exception {
        Common.logger.finer("begin");

        DataInputStream inputStream;
        try {
            inputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(sysexFile)));
        } catch (FileNotFoundException ex) {
            Common.logger.severe("FileNotFoundException");
            throw ex;
        }

        Common.logger.finer("build kits");

        // build kits
        for (int kitNumber = 0; kitNumber < Device.COUNTKIT; kitNumber++) {
            Common.logger.log(Level.FINER, "kitNumber<{0}>, Status<{1}>", new Object[]{kitNumber, Common.unsignedByteToInt(inputStream.readByte())}); //0xF0
            kits[kitNumber] = new Kit(readStream(inputStream, Kit.KITMESSAGELENGTH));
        }

        Common.logger.finer("build inputs");

        for (int inputNumber = 0; inputNumber < Device.COUNTINPUT; inputNumber++) {
            Common.logger.log(Level.FINER, "inputNumber<{0}>, Status<{1}>", new Object[]{inputNumber, Common.unsignedByteToInt(inputStream.readByte())}); //0xF0
            triggerInputs[inputNumber] = new Input(readStream(inputStream, Input.INPUTMESSAGELENGTH));
        }

        Common.logger.finer("build input names");
        
        byte[] byteStreamInputNames = new byte[Device.BEGININPUTNAMES.getBytes().length];

        try{
            inputStream.read(byteStreamInputNames, 0, byteStreamInputNames.length);
        } catch (IOException ex) {
            Common.logger.finer("IOException");
        }

        if (new String(byteStreamInputNames).equalsIgnoreCase(Device.BEGININPUTNAMES)) {

            for (int inputNumber = 0; inputNumber < Device.COUNTINPUT; inputNumber++) {
                byte[] byteStream = new byte[Device.INPUTNAMELENGTH];
                inputStream.read(byteStream, 0, byteStream.length);

                triggerInputs[inputNumber].setTriggerInputName(new String(byteStream));
            }
        }
        else{
            Common.logger.finer("No names found");
        }

        inputStream.close();
        Common.logger.finer("end");
    }
}