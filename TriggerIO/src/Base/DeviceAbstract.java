/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Base;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author dan
 */
public abstract class DeviceAbstract <T extends Kit, U extends GlobalInput>{

    public static final String ROOT = "device";
    public List<T> kits = new ArrayList<T>();
    public List<U> globalInputs = new ArrayList<U>();

    //---------------------------------------------------------------------
    public DeviceAbstract() {
    }

    //---------------------------------------------------------------------
    public Element getDevice(Document doc) {
        Element element = doc.createElement(ROOT);

        for (T kit : kits) {
            element.appendChild(kit.getKit(doc));
        }

        for (U input : this.globalInputs) {
            element.appendChild(input.getInput(doc));
        }

        return element;
    }

    abstract protected void addKit(Element element, int i);
    abstract protected void addGlobalInput(Element element, int i);

    //---------------------------------------------------------------------
    public void setDevice (Element element) {
        Common.logger.fine("begin");

        this.kits.clear();
        this.globalInputs.clear();

        // kits
        {
            NodeList nodes = element.getElementsByTagName(Kit.ROOT);
            Common.logger.log(Level.FINE, "kitNodes.length<{0}>", nodes.getLength());

            for (int i = 0; i < nodes.getLength(); i++) {
                addKit((Element) nodes.item(i), i);

                Common.logger.log(Level.FINE, "kits(i)<{0}>, i=<{1}>", new Object[]{this.kits.size(), i});
            }

            Common.logger.log(Level.FINE, "kits.size<{0}>", this.kits.size());

        }

        // global inputs
        {
            NodeList nodes = element.getElementsByTagName(GlobalInput.ROOT);
            Common.logger.log(Level.FINE, "globalInputNodes.length<{0}>", nodes.getLength());

            for (int i = 0; i < nodes.getLength(); i++) {
                addGlobalInput((Element) nodes.item(i), i);
            }

            Common.logger.log(Level.FINE, "globalInputs.size<{0}>", this.globalInputs.size());
        }
    }
}