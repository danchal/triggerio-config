package Server;

import Midi.DeviceMidi;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Protocol {

    public static final String ROOT = "message";
    public static final String ACTION_GET = "get";
    public static final String ACTION_SET = "set";
    public static final String ACTION_OK = "ok";
    public static final String ACTION_CLOSE = "close";
    public static final String ACTIONTYPE = "actiontype";

    public enum StatusType {
        CLOSE, OK
    }

    public StatusType status;
    public String outMessage;
    public Document outDocument;

    private Document createDocument(String actionType) throws ParserConfigurationException {
        //Create instance of DocumentBuilderFactory
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //Get the DocumentBuilder
        DocumentBuilder docBuilder = factory.newDocumentBuilder();
        //Create blank DOM Document
        Document document = docBuilder.newDocument();

        //create the root element
        Element message = document.createElement(ROOT);

        //set message action type
        message.setAttribute(ACTIONTYPE, actionType);

        //add it to the xml tree
        document.appendChild(message);
        return document;
    }

    public Protocol(Document inDoc, DeviceMidi device) throws ParserConfigurationException {
        Element messageElement = inDoc.getElementById(ROOT);

        String actionType = messageElement.getAttribute(ACTIONTYPE);

        status = StatusType.OK;

        if (ACTION_GET.equalsIgnoreCase(actionType)) {
            outDocument = createDocument(ACTION_SET);
            outDocument.appendChild(device.getDevice(outDocument));

        } else if (ACTION_SET.equalsIgnoreCase(actionType)) {
            device.setDevice(inDoc.getElementById(DeviceMidi.ROOT));
            outDocument = createDocument(ACTION_OK);

        } else if (ACTION_CLOSE.equalsIgnoreCase(actionType)) {
            status = StatusType.CLOSE;
        }
    }
}