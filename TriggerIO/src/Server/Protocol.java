package Server;

import Base.Common;
import Midi.DeviceMidi;
import java.util.logging.Level;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Protocol {

    public static final String ROOT = "message";
    public static final String ACTION_GET = "get";
    public static final String ACTION_SET = "set";
    public static final String ACTION_OK = "ok";
    public static final String ACTION_CLOSE = "close";
    public static final String ACTIONTYPE = "actiontype";

    public enum StatusType {
        CLOSE, OK, FAIL
    }

    public StatusType status;
    public Document outDocument;

    public Protocol(Document inDoc, DeviceMidi device) throws ParserConfigurationException {
        Base.Common.logger.log(Level.INFO, "begin");

        String actionType;

        {
            NodeList nodeList = inDoc.getElementsByTagName(Protocol.ROOT);
            Element messageElement = (Element) nodeList.item(0);
            actionType = messageElement.getAttribute(ACTIONTYPE);
        }

        Base.Common.logger.log(Level.FINE, "actionType=<{0}>", actionType);

        status = StatusType.OK;

        if (ACTION_GET.equalsIgnoreCase(actionType)) {
            outDocument = Tools.createDocument(ACTION_SET);

            Element rootElement = outDocument.getDocumentElement();
            rootElement.appendChild(device.getDevice(outDocument));

        } else if (ACTION_SET.equalsIgnoreCase(actionType)) {
            NodeList nodeList = inDoc.getElementsByTagName(DeviceMidi.ROOT);
            Element deviceElement = (Element) nodeList.item(0);

            device.setDevice(deviceElement);
            outDocument = Tools.createDocument(ACTION_OK);

        } else if (ACTION_CLOSE.equalsIgnoreCase(actionType)) {
            status = StatusType.CLOSE;
        }
    }
}