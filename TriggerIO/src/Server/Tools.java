/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author dan
 */
public class Tools {

    public static Document createDocument(String actionType) throws ParserConfigurationException {
        //Create instance of DocumentBuilderFactory
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //Get the DocumentBuilder
        DocumentBuilder docBuilder = factory.newDocumentBuilder();
        //Create blank DOM Document
        Document document = docBuilder.newDocument();

        //create the root element
        Element message = document.createElement(Protocol.ROOT);

        //set message action type
        message.setAttribute(Protocol.ACTIONTYPE, actionType);

        //add it to the xml tree
        document.appendChild(message);
        return document;
    }

    public static String toString(Document docMessage) {
        String returnString = null;
        try {
            Source source = new DOMSource(docMessage);
            StringWriter stringWriter = new StringWriter();
            Result result = new StreamResult(stringWriter);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.transform(source, result);
            returnString = stringWriter.getBuffer().toString();
        } catch (TransformerException ex) {
            Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, ex);
        }

        return returnString;
    }
}
