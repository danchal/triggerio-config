package Server;

import Midi.DeviceMidi;
import java.util.logging.Level;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Protocol {

    public static final String ROOT = "message";
    public static final String ACTION_GET = "get";
    public static final String ACTION_SET = "set";
    public static final String ACTION_UPDATE = "update";
    public static final String ACTION_OK = "ok";
    public static final String ACTION_CLOSE = "close";
    public static final String ACTIONTYPE = "actiontype";

    public enum StatusType {
        CLOSE, OK, FAIL
    }

    public StatusType status;
    public Document response;

    public Protocol(Document request, DeviceMidi device) throws ParserConfigurationException, InvalidMidiDataException, MidiUnavailableException {
        Base.Common.logger.log(Level.INFO, "begin");

        String actionType;

        {
            NodeList nodeList = request.getElementsByTagName(Protocol.ROOT);
            Element messageElement = (Element) nodeList.item(0);
            actionType = messageElement.getAttribute(ACTIONTYPE);
        }

        Base.Common.logger.log(Level.FINE, "actionType=<{0}>", actionType);

        status = StatusType.OK;

        if (ACTION_GET.equalsIgnoreCase(actionType)) {
            response = Tools.createDocument(ACTION_SET);

            Element rootElement = response.getDocumentElement();
            rootElement.appendChild(device.get(response));

        } else if (ACTION_SET.equalsIgnoreCase(actionType)) {
            NodeList nodeList = request.getElementsByTagName(DeviceMidi.ROOT);
            Element deviceElement = (Element) nodeList.item(0);

            device.set(deviceElement);
            device.send();
            response = Tools.createDocument(ACTION_OK);

        } else if (ACTION_UPDATE.equalsIgnoreCase(actionType)) {
            NodeList nodeList = request.getElementsByTagName(DeviceMidi.ROOT);
            Element deviceElement = (Element) nodeList.item(0);

            device.update(deviceElement);
            response = Tools.createDocument(ACTION_OK);

        } else if (ACTION_CLOSE.equalsIgnoreCase(actionType)) {
            status = StatusType.CLOSE;
        }
    }
}