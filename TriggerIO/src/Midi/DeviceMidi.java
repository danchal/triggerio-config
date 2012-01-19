/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Midi;

import Base.Device;
import java.io.*;
import java.util.logging.Level;
import javax.sound.midi.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author DrumTrigger
 */
public class DeviceMidi extends Device {

    private MidiDevice midiDevice;
    protected static Exception exBadByteLength;

    //---------------------------------------------------------------------
    public DeviceMidi() {
        super();
    }

    //----------------------------------------------
    public DeviceMidi(File sysexFile) throws UserException, Exception {
        importSysex(sysexFile);
    }

    //---------------------------------------------------------------------
    public MidiDevice.Info getInfo() throws Exception {
        return this.midiDevice.getDeviceInfo();
    }

    //---------------------------------------------------------------------
    public boolean isOpen() {
        boolean isOpen = false;

        try {
            isOpen = this.midiDevice.isOpen();
        } catch (Exception e) {
        }
        Common.logger.log(Level.FINEST, "return=<{0}>", String.valueOf(isOpen));
        return isOpen;
    }

    //---------------------------------------------------------------------
    public void close() {
        this.midiDevice.close();
        this.midiDevice = null;
        Common.logger.info("device closed");
    }

    //---------------------------------------------------------------------
    public void open(MidiDevice.Info info) throws MidiUnavailableException {
        this.midiDevice = MidiSystem.getMidiDevice(info);
        this.midiDevice.open();
        Common.logger.log(Level.INFO, "device opened=<{0}>", this.midiDevice.getDeviceInfo().getName());
    }

    //---------------------------------------------------------------------
    public void sendtKit(int kitNumber) throws MidiUnavailableException, InvalidMidiDataException {
        Receiver receiver = midiDevice.getReceiver();
        sendtKit(kitNumber, receiver);
    }

    //---------------------------------------------------------------------
    public void sendtKit(int kitNumber, Receiver receiver) throws InvalidMidiDataException {
        Common.logger.log(Level.FINE, "Sending kitNumber=<{0}>", kitNumber);
        receiver.send(KitMidi.getSysexMessage(kits[kitNumber]), -1);
    }

    //---------------------------------------------------------------------
    public void sendTriggerInput(int inputNumber) throws MidiUnavailableException, InvalidMidiDataException {
        Receiver receiver = midiDevice.getReceiver();
        sendTriggerInput(inputNumber, receiver);
    }

    //---------------------------------------------------------------------
    public void sendTriggerInput(int inputNumber, Receiver receiver) throws InvalidMidiDataException {
        Common.logger.log(Level.FINE, "Sending inputNumber=<{0}>", inputNumber);
        receiver.send(((GlobalInputMidi) (globalInputs[inputNumber])).getSysexMessage(), -1);
    }

    //---------------------------------------------------------------------
    public void send() throws InvalidMidiDataException, MidiUnavailableException {
        Common.logger.fine("begin");
        Receiver receiver = midiDevice.getReceiver();

        for (int kitNumber = 0; kitNumber < kits.length; kitNumber++) {
            sendtKit(kitNumber, receiver);
        }

        for (int inputNumber = 0; inputNumber < globalInputs.length; inputNumber++) {
            sendTriggerInput(inputNumber, receiver);
        }

        receiver.close();
        Common.logger.fine("end");
    }

    //---------------------------------------------------------------------
    public final void exportXml(File file) throws FileNotFoundException, IOException, TransformerConfigurationException, ParserConfigurationException, TransformerException {
        Common.logger.fine("begin");

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.newDocument();

        doc.appendChild(getDevice(doc));

        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(file);

        transformer.transform(source, result);
    }

    //---------------------------------------------------------------------
    public final void importXml(File file) throws Exception {
        Common.logger.fine("begin");
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            Common.logger.fine("before parse");
            Document doc = db.parse(file);

            setDevice(doc.getDocumentElement());

        } catch (SAXException ex) {
            Common.logger.severe("Not a valid xml file");
            throw ex;
        }
        catch (FileNotFoundException ex) {
            Common.logger.severe("FileNotFoundException");
            throw ex;
        }
    }

    //---------------------------------------------------------------------
    public final void exportSyx(File file) throws FileNotFoundException, IOException {
        Common.logger.fine("begin");
        DataOutputStream outputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));

        for (int kitNumber = 0; kitNumber < kits.length; kitNumber++) {
            outputStream.write(0xF0);
            outputStream.write(KitMidi.getByteMessage(kits[kitNumber]));
        }
        for (int inputNumber = 0; inputNumber < globalInputs.length; inputNumber++) {
            outputStream.write(0xF0);
            outputStream.write(((GlobalInputMidi) (globalInputs[inputNumber])).getByteMessage(), 0, GlobalInputMidi.INPUTMESSAGELENGTH);
        }

        outputStream.close();
        Common.logger.fine("end");
    }

    //---------------------------------------------------------------------
    private static byte[] readStream(DataInputStream inputStream, int length) throws IOException {
        byte[] byteStream = new byte[length];

        inputStream.read(byteStream, 0, byteStream.length);

        Common.logger.finer(Common.printMessage(byteStream));
        return byteStream;
    }

    //----------------------------------------------
    public final void importSysex(File sysexFile) throws UserException, FileNotFoundException, Exception {
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
        for (int kitNumber = 0; kitNumber < DeviceMidi.COUNTKIT; kitNumber++) {
            Common.logger.log(Level.FINER, "kitNumber<{0}>, Status<{1}>", new Object[]{kitNumber, Common.unsignedByteToInt(inputStream.readByte())}); //0xF0
            kits[kitNumber] = new KitMidi(readStream(inputStream, KitMidi.KITMESSAGELENGTH));
        }

        Common.logger.finer("build inputs");

        for (int inputNumber = 0; inputNumber < DeviceMidi.COUNTINPUT; inputNumber++) {
            Common.logger.log(Level.FINER, "inputNumber<{0}>, Status<{1}>", new Object[]{inputNumber, Common.unsignedByteToInt(inputStream.readByte())}); //0xF0
            globalInputs[inputNumber] = new GlobalInputMidi(readStream(inputStream, GlobalInputMidi.INPUTMESSAGELENGTH));
        }

        inputStream.close();
        Common.logger.finer("end");
    }
}