
import Base.Device;
import Midi.Common;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dan
 */
public class TestGetKit {
    public static void main(String[] args) {
        System.out.println("******************************************************************");


        System.out.println("begin");

        Common.setLogLevel(Level.ALL);

        Device device = new Device();

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db;


             db = dbf.newDocumentBuilder();

            System.out.println("before parse");
            Document doc = db.parse(new File("/home/dan/Downloads/test.tct"));

            System.out.println("after parse");

            device.setDevice(doc.getDocumentElement());

        } catch (IOException ex) {
            Logger.getLogger(TestGetKit.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Common.logger.severe("Not a valid xml file");
        } catch (ParserConfigurationException ex) {
                Logger.getLogger(TestGetKit.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}

