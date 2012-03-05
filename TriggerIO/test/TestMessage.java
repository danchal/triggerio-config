
import Base.UserException;
import Midi.DeviceMidi;
import Server.Protocol;
import Server.Tools;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author dan
 */
public class TestMessage {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {

            {
                Document request = Tools.createDocument(Protocol.ACTION_GET);
                Base.Common.logger.log(Level.INFO, "request={0}", Tools.toString(request));

                NodeList nodeList = request.getElementsByTagName(Protocol.ROOT);
                Element messageElement = (Element) nodeList.item(0);

                String actionType = messageElement.getAttribute(Protocol.ACTIONTYPE);
                Base.Common.logger.log(Level.INFO, "actionType={0}", actionType);
            }

            //--------------------

            {
                DeviceMidi device = null;
                Document response = Tools.createDocument(Protocol.ACTION_SET);

                Element rootElement = response.getDocumentElement();
                Element element = response.createElement(DeviceMidi.ROOT);
                rootElement.appendChild(element);

                Base.Common.logger.log(Level.INFO, "response={0}", Tools.toString(response));

                String responseActionType;
                NodeList nodeList = response.getElementsByTagName(Protocol.ROOT);
                Element messageElement = (Element) nodeList.item(0);
                responseActionType = messageElement.getAttribute(Protocol.ACTIONTYPE);

                Base.Common.logger.log(Level.INFO, "responseActionType={0}", responseActionType);

            }

        } catch (UserException ex) {
            Logger.getLogger(TestMessage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
