/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Base;

import java.util.logging.Level;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author dan
 */
public class Device {

    public static final int COUNTKIT = 21;
    public static final int COUNTINPUT = 21;
    public static final String ROOT = "device";
    public Kit[] kits = new Kit[COUNTKIT];
    public GlobalInput[] globalInputs = new GlobalInput[COUNTINPUT];

    //---------------------------------------------------------------------
    public Device(Element element) {
        setDevice(element);
    }

    //---------------------------------------------------------------------
    public Device() {
    }

    //---------------------------------------------------------------------
    public Element getDevice(Document doc) {
        Element element = doc.createElement(ROOT);

        for (Kit kit : kits) {
            element.appendChild(kit.getKit(doc));
        }

        for (GlobalInput input : globalInputs) {
            element.appendChild(input.getInput(doc));
        }

        return element;
    }

    //---------------------------------------------------------------------
    public final void setDevice(Element element) {
        Common.logger.fine("begin");
        // kits
        {
            NodeList nodes = element.getElementsByTagName(Kit.ROOT);
            Common.logger.log(Level.FINE, "kitNodes.length <{0}>", nodes.getLength());

            this.kits = new Kit[COUNTKIT];

            for (int i = 0; i < nodes.getLength(); i++) {
                kits[i] = new Kit((Element) nodes.item(i));
            }
        }

        // global inputs
        {
            NodeList nodes = element.getElementsByTagName(GlobalInput.ROOT);
            Common.logger.log(Level.FINE, "globalInputNodes.length <{0}>", nodes.getLength());

            this.globalInputs = new GlobalInput[COUNTINPUT];
            for (int i = 0; i < nodes.getLength(); i++) {
                globalInputs[i] = new GlobalInput((Element) nodes.item(i));
            }
        }
    }
}