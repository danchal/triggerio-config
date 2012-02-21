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
    public static final Map<Integer, String> lovProgramChange = new LinkedHashMap<Integer, String>();
    private String kitName;
    private int kitNumber;
    private int programChange;
    public List<Input> inputs = new ArrayList<Input>();

    //---------------------------------------------------------------------
    static {
        BuildProgramChangeLov();
    }

    //---------------------------------------------------------------------
    protected Kit() {
    }

    //----------------------------------------------
    public Kit(Element element) {
        set(element);
    }

    //---------------------------------------------------------------------
    private Element getKit(Document doc, int inputNumber, boolean getAll) {
        Element element = doc.createElement(ROOT);
        element.setAttribute(PNAME, kitName);
        element.setAttribute(PNUMBER, String.valueOf(kitNumber));
        element.setAttribute(PPROGRAM, String.valueOf(programChange));

        for (Input input : inputs) {
            if ((input.getinputNumber() == inputNumber) || getAll){
                element.appendChild(input.get(doc, Input.Setting.ALL));
            }
        }

        return element;
    }

    //---------------------------------------------------------------------
    public Element get(Document doc, int inputNumber) {
        return getKit(doc, inputNumber, false);
    }

    //---------------------------------------------------------------------
    public Element get(Document doc) {
        return getKit(doc, -1, true);
    }

    //---------------------------------------------------------------------
    public final void set(Element element) {
        setKitNumber(Integer.parseInt(element.getAttribute(PNUMBER)));
        setKitName(element.getAttribute(PNAME));
        setProgramChange(Integer.parseInt(element.getAttribute(PPROGRAM)));

        NodeList inputNodes = element.getElementsByTagName(Input.ROOT);
        Common.logger.log(Level.FINE, "inputNodes.length <{0}>", inputNodes.getLength());

        inputs.clear();

        for (int i = 0; i < inputNodes.getLength(); i++) {
            inputs.add(i, new Input((Element) inputNodes.item(i)));
        }
    }

    //----------------------------------------------
    public String getKitName() {
        return this.kitName;
    }

    //----------------------------------------------
    public int getKitNumber() {
        return this.kitNumber;
    }

    //----------------------------------------------
    public int getProgramChange() {
        return this.programChange;
    }

    //----------------------------------------------
    protected void setKitNumber(int kitNumber) {
        this.kitNumber = kitNumber;
        Common.logger.log(Level.FINEST, "kitNumber<{0}>", this.kitNumber);
    }

    //----------------------------------------------
    public void setKitName(String kitName) {
        this.kitName = kitName;
        Common.logger.log(Level.FINEST, "kitNumber<{0}>, kitName<{1}>", new Object[]{this.kitNumber, this.kitName});
    }

    //----------------------------------------------
    public void setProgramChange(int programChange) {
        if (lovProgramChange.containsKey(programChange)) {
            this.programChange = programChange;
            Common.logger.log(Level.FINEST, "kitNumber<{0}>, programChange<{1}>", new Object[]{this.kitNumber, this.programChange});
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
