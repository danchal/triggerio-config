
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.SysexMessage;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author DrumTrigger@gmail.com
 */
public class TestDeviceInfos {

	static private Receiver receiver;

	static void sendJavaMidi(int status, byte[] data) {

		if (receiver == null) return;

		try{

			MidiMessage msg = null;

			if (status == 0xF0) {

				msg = new javax.sound.midi.SysexMessage();

				try{ ((SysexMessage)msg).setMessage(0xF0, data, data.length); }catch (InvalidMidiDataException imde){return;}

			} else if (status >= 250) {

				msg = new ShortMessage();

				try{((ShortMessage)msg).setMessage(status); }catch (InvalidMidiDataException imde){return;}

			} else {

				msg = new ShortMessage();

				try{((ShortMessage)msg).setMessage(status, data[0], data[1]); }catch (InvalidMidiDataException imde){return;}

			}

			if (msg == null) return;

			receiver.send(msg, 0);

		}catch (Exception e){}

	}
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("******************************************************************");

        int selectedIndex = 0;

        MidiDevice.Info[] allDevices = MidiSystem.getMidiDeviceInfo();

        for (int i = 0; i < allDevices.length; i++) {
            try {
                MidiDevice testDevice = MidiSystem.getMidiDevice(allDevices[i]);
//                if (!(testDevice instanceof Sequencer) && !(testDevice instanceof Synthesizer)) {
//                    if ((transmitting && testDevice.getMaxTransmitters() != 0)
//                            || (!transmitting && testDevice.getMaxReceivers() != 0)) {


                        System.out.println(""
                                + " "  + i
                                + ", " + (testDevice instanceof Sequencer  ? "Sequencer" : "")
                                + ", " + (testDevice instanceof Synthesizer ? "Synthesizer" : "")
                                + ", " + testDevice.getMaxTransmitters()
                                + ", " + testDevice.getMaxReceivers()
                                + ", " + testDevice.getDeviceInfo()
                                + ", " + allDevices[i].getName()
                                + ", " + allDevices[i].getDescription()
                                + ", " + allDevices[i].getVendor()
                                + ", " + allDevices[i].getVersion()
                                );

                        if (!(testDevice instanceof Sequencer || testDevice instanceof Synthesizer)
                                && (testDevice.getMaxReceivers() != 0) ){
                            selectedIndex = i;
                            System.out.println("selectedIndex =<" + selectedIndex + ">");
                        }

            } catch (MidiUnavailableException ex) {
                System.out.println("Midi Device Unavailable: " + allDevices[i].getDescription());
            }
        }

       BufferedReader stdin = new BufferedReader
      (new InputStreamReader(System.in));

        System.out.println("Select device:");
        System.out.flush(); // empties buffer, before you input text
        try {selectedIndex = Integer.parseInt(stdin.readLine());} catch (IOException ex) {Logger.getLogger(TestDeviceInfos.class.getName()).log(Level.SEVERE, null, ex);}
        
        System.out.println("selectedIndex =<" + selectedIndex + ">");

        if (selectedIndex== 0){
            System.out.println("No receivers found!!!!!!");
        }
        else{
            try{
                MidiDevice myOutPort = javax.sound.midi.MidiSystem.getMidiDevice(allDevices[selectedIndex]);
                myOutPort.open();
                receiver = myOutPort.getReceiver();
            }catch (MidiUnavailableException mue){
                    System.out.println("can't open MidiOut : " +mue.toString());
            }

            try{
                File f = new File(System.getProperty("user.home"), "sysex.syx");

                java.io.FileInputStream fis = new java.io.FileInputStream(f.getAbsoluteFile());
                byte[] data = new byte[(int)(f.length())];
                int offset = 0;
                while(offset < data.length) {
                        int num = fis.read(data, offset, fis.available());
                        offset+= num;
                }


                if ((data[0] & 0xFF) == 0xF0) {
                        byte[] outData = new byte[data.length-1];
                        for (int i = 1; i < data.length; i++) outData[i-1] = data[i];
                        sendJavaMidi(240, outData);
                } else sendJavaMidi(240, data);

            } catch (Exception ex){ex.printStackTrace();}
        }
    }
}
