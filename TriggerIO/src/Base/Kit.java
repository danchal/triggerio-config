/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Base;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author dan
 */
public class Kit {

    public static final int NOPROGRAMCHANGE = -1;
    public static final String NOPROGRAMCHANGE_VAL = "---";
    public static final String ROOT = "kit";
    public static final String PNUMBER = "number";
    public static final String PNAME = "name";
    public static final String PPROGRAM = "program";
    public static final String PINPUT = "input";
    public static final Map<Integer, String> lovProgramChange = new LinkedHashMap<Integer, String>();
    private String kitName;
    private int kitNumber;
    private int programChange;
    public List<Input> inputs;

    //---------------------------------------------------------------------
    static {
        BuildProgramChangeLov();
    }

    //---------------------------------------------------------------------
    protected Kit() {
    }

    //----------------------------------------------
    public Kit(int kitNumber, String kitName, int programChange,
            List<Input> inputs) {
    }

    //----------------------------------------------
    public Kit(Element element) {
        setKit(element);
    }

    //---------------------------------------------------------------------
    public Element getKit(Document doc) {
        Element element = doc.createElement(ROOT);
        element.setAttribute(PNAME, kitName);
        element.setAttribute(PNUMBER, String.valueOf(kitNumber));
        element.setAttribute(PPROGRAM, String.valueOf(programChange));

        for (Input input : inputs) {
            element.appendChild(input.getInput(doc));
        }

        return element;
    }

    //---------------------------------------------------------------------
    public final void setKit(Element element) {
        setKitNumber(Integer.parseInt(element.getAttribute(PNUMBER)));
        setKitName(element.getAttribute(PNAME));
        setProgramChange(Integer.parseInt(element.getAttribute(PPROGRAM)));

        NodeList inputNodes = element.getElementsByTagName(PINPUT);
        Common.logger.log(Level.FINE, "inputNodes.length <{0}>", inputNodes.getLength());

        inputs = new ArrayList<Input>();

        for (int i = 0; i < inputNodes.getLength(); i++) {
            inputs.add(new Input((Element) inputNodes.item(i)));
        }
    }

    //----------------------------------------------
    public final String getKitName() {
        return this.kitName;
    }

    //----------------------------------------------
    public final int getKitNumber() {
        return this.kitNumber;
    }

    //----------------------------------------------
    public final int getProgramChange() {
        return this.programChange;
    }

    //----------------------------------------------
    protected void setKitNumber(int kitNumber) {
        this.kitNumber = kitNumber;
        Common.logger.log(Level.FINEST, "kitNumber<{0}>", this.kitNumber);
    }

    //----------------------------------------------
    public final void setKitName(String kitName) {
        this.kitName = kitName;
        Common.logger.log(Level.FINEST, "kitName<{0}>", this.kitName);
    }

    //----------------------------------------------
    public final void setProgramChange(int programChange) {
        if (lovProgramChange.containsKey(programChange)) {
            this.programChange = programChange;
            Common.logger.log(Level.FINEST, "programChange<{0}>", this.programChange);
        } else {
            Common.logger.log(Level.WARNING, "Unrecognised programChange<{0}>", programChange);
        }
    }

    //----------------------------------------------
    private static void BuildProgramChangeLov() {
        lovProgramChange.put(Kit.NOPROGRAMCHANGE, Kit.NOPROGRAMCHANGE_VAL);

        for (int i = 0; i <= 127; i++) {
            lovProgramChange.put(i, Integer.toString(i));
        }
    }
}
