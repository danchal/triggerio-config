/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Base.Common;
import Base.UserException;
import java.io.IOException;
import java.io.StringReader;
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
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author dan
 */
public class Tools {

    public static Document createDocument(String actionType) throws UserException {
        //Create instance of DocumentBuilderFactory
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //Get the DocumentBuilder
        DocumentBuilder docBuilder;
        try {
            docBuilder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            throw new UserException(ex.toString());
        }
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

    public static Document toXml(String xmlSource) throws UserException {
        Document xml = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            xml = builder.parse(new InputSource(new StringReader(xmlSource)));
        } catch (IOException ex) {
            Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, ex);
            throw new UserException(ex.toString());
        } catch (SAXException ex) {
            Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, ex);
            throw new UserException(ex.toString());
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, ex);
            throw new UserException(ex.toString());
        }
        return xml;
    }

    public static String toString(Document docMessage) throws UserException {
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
            throw new UserException(ex.toString());
        }

        return returnString;
    }

    // ----------------------------------------------------------------
    public static boolean stopThread(Thread thread) {
        Common.logger.log(Level.FINE, "name=<{0}>", new Object[]{thread.getName()});
        boolean wasStopped = false;

        if (thread.isAlive()) {
            Common.logger.fine("isAlive");
            thread.interrupt();

            while (thread.isAlive()) {
                try {
                    thread.join(1000);
                } catch (InterruptedException ex) {
                    Common.logger.fine("interrupted");
                }
            }
            wasStopped = true;

        } else {
            Common.logger.fine("thread not running");
        }
        Common.logger.log(Level.FINE, "wasStopped =<{0}>, <{1}>", new Object[] {wasStopped, thread.getName()});
        return wasStopped;
    }
}
