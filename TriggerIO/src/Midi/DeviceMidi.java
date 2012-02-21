/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Midi;

import Base.DeviceAbstract;
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
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author DrumTrigger
 */
public class DeviceMidi extends DeviceAbstract<KitMidi, GlobalInputMidi>  {

    private MidiDevice midiDevice;
    protected static Exception exBadByteLength;
    public static final int COUNTKIT = 21;
    public static final int COUNTINPUT = 21;

    //---------------------------------------------------------------------
    public DeviceMidi() {
    }

    //----------------------------------------------
    public DeviceMidi(File sysexFile) throws UserException, FileNotFoundException, IOException {
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
    public void sendKit(int kitNumber) throws MidiUnavailableException, InvalidMidiDataException {
        Receiver receiver = midiDevice.getReceiver();
        getKit(kitNumber).send(receiver);
        receiver.close();
    }

    //---------------------------------------------------------------------
    public void sendTriggerInput(int inputNumber) throws MidiUnavailableException, InvalidMidiDataException {
        Receiver receiver = midiDevice.getReceiver();
        getGlobalInput(inputNumber).send(receiver);
        receiver.close();
    }

    //---------------------------------------------------------------------
    public void send() throws InvalidMidiDataException, MidiUnavailableException {
        Common.logger.fine("begin");
        Receiver receiver = midiDevice.getReceiver();

        for (KitMidi kit : kits){
            kit.send(receiver);
        }

        for (GlobalInputMidi globalInput : globalInputs){
            globalInput.send(receiver);
        }

        receiver.close();
        Common.logger.fine("end");
    }

    //---------------------------------------------------------------------
    public void update (Element element) throws MidiUnavailableException, InvalidMidiDataException {
        NodeList inputNodes = element.getElementsByTagName(KitMidi.ROOT);
        Common.logger.log(Level.FINE, "inputNodes.length <{0}>", inputNodes.getLength());

        Receiver receiver = midiDevice.getReceiver();

        for (int i = 0; i < inputNodes.getLength(); i++) {
            Element kitElement = (Element) inputNodes.item(i);
            int kitNumber = Integer.parseInt(kitElement.getAttribute(KitMidi.PNUMBER));

            for (KitMidi kit : kits){
                if (kit.getKitNumber() == kitNumber){
                    kit.update(kitElement, receiver);
                }
            }
        }
        receiver.close();
    }

    //---------------------------------------------------------------------
    public final void exportXml(File file) throws FileNotFoundException, IOException, TransformerConfigurationException, ParserConfigurationException, TransformerException {
        Common.logger.fine("begin");

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.newDocument();

        doc.appendChild(get(doc));

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

            set(doc.getDocumentElement());

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

        for (KitMidi kit : kits) {
            outputStream.write(0xF0);
            outputStream.write(kit.getByteMessage());
        }

        for (GlobalInputMidi globalInput : globalInputs) {
            outputStream.write(0xF0);
            outputStream.write(globalInput.getByteMessage(), 0, GlobalInputMidi.INPUTMESSAGELENGTH);
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
    public final void importSysex(File sysexFile) throws UserException, FileNotFoundException, IOException {
        Common.logger.finer("begin");

        DataInputStream inputStream;
        try {
            inputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(sysexFile)));
        } catch (FileNotFoundException ex) {
            Common.logger.severe("FileNotFoundException");
            throw ex;
        }

        Common.logger.finer("build kits");

        kits.clear();

        // build kits
        for (int kitNumber = 0; kitNumber < DeviceMidi.COUNTKIT; kitNumber++) {
            Common.logger.log(Level.FINER, "kitNumber<{0}>, Status<{1}>", new Object[]{kitNumber, Common.unsignedByteToInt(inputStream.readByte())}); //0xF0
            kits.add(kitNumber, new KitMidi(readStream(inputStream, KitMidi.KITMESSAGELENGTH)));
        }

        Common.logger.finer("build inputs");

        globalInputs.clear();

        // build inputs
        for (int inputNumber = 0; inputNumber < DeviceMidi.COUNTINPUT; inputNumber++) {
            Common.logger.log(Level.FINER, "inputNumber<{0}>, Status<{1}>", new Object[]{inputNumber, Common.unsignedByteToInt(inputStream.readByte())}); //0xF0
            globalInputs.add(inputNumber, new GlobalInputMidi(readStream(inputStream, GlobalInputMidi.INPUTMESSAGELENGTH)));
        }

        inputStream.close();
        Common.logger.finer("end");
    }

    @Override
    protected void addKit(Element element, int i) {
        kits.add(i, new KitMidi(element));
    }

    @Override
    protected void addGlobalInput(Element element, int i) {
        globalInputs.add(i, new GlobalInputMidi(element));
    }
}