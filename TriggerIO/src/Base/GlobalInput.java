/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Base;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author dan
 */
public class GlobalInput {

    private String name;
    private int number;
    private int gain;
    private int velocity;
    private int threshold;
    private int xTalk;
    private int retrigger;
    private int type;
    public static final String ROOT = "globalinput";
    public static final String PNUMBER = "number";
    public static final String PNAME = "name";
    public static final String PGAIN = "gain";
    public static final String PVELOCITY = "velocity";
    public static final String PTHRESHOLD = "threshold";
    public static final String PXTALK = "xTalk";
    public static final String PRETRIGGER = "retrigger";
    public static final String TYPE = "triggerType";
    public static final Map<Integer, String> lovGain = new LinkedHashMap<Integer, String>();
    public static final Map<Integer, String> lovVelocity = new LinkedHashMap<Integer, String>();
    public static final Map<Integer, String> lovThreshold = new LinkedHashMap<Integer, String>();
    public static final Map<Integer, String> lovRetrigger = new LinkedHashMap<Integer, String>();
    public static final Map<Integer, String> lovXTalk = new LinkedHashMap<Integer, String>();
    public static final Map<Integer, String> lovType = new LinkedHashMap<Integer, String>();

    static {
        BuildLov();
    }

    protected GlobalInput() {
    }

    //----------------------------------------------
    public GlobalInput(int triggerInputNumber, String triggerInputName,
            int gain, int velocityCurve, int threshold,
            int xTalk, int retrigger, int triggerType) {

        setTriggerInputNumber(triggerInputNumber);
        setTriggerInputName(triggerInputName);
        setGain(gain);
        setVelocityCurve(velocityCurve);
        setThreshold(threshold);
        setXTalk(xTalk);
        setRetrigger(retrigger);
        setTriggerType(triggerType);
    }

    public GlobalInput(Element element) {
        setInput(element);
    }

    //---------------------------------------------------------------------
    public Element getInput(Document doc) {
        Element element = doc.createElement(ROOT);

        element.setAttribute(PNUMBER, String.valueOf(number));
        element.setAttribute(PNAME, name);

        element.setAttribute(PGAIN, String.valueOf(gain));
        element.setAttribute(PVELOCITY, String.valueOf(velocity));
        element.setAttribute(PTHRESHOLD, String.valueOf(threshold));
        element.setAttribute(PXTALK, String.valueOf(xTalk));
        element.setAttribute(PRETRIGGER, String.valueOf(retrigger));
        element.setAttribute(TYPE, String.valueOf(type));

        return element;
    }

    public final void setInput(Element element) {
        setTriggerInputNumber(Integer.parseInt(element.getAttribute(PNUMBER)));
        setTriggerInputName(element.getAttribute(PNAME));

        setGain(Integer.parseInt(element.getAttribute(PGAIN)));
        setVelocityCurve(Integer.parseInt(element.getAttribute(PVELOCITY)));
        setThreshold(Integer.parseInt(element.getAttribute(PTHRESHOLD)));
        setXTalk(Integer.parseInt(element.getAttribute(PXTALK)));
        setRetrigger(Integer.parseInt(element.getAttribute(PRETRIGGER)));
        setTriggerType(Integer.parseInt(element.getAttribute(TYPE)));
    }

    public final String getInputName(int inputNumber) {
        return name;
    }

    //----------------------------------------------
    public final String getTriggerInputName() {
        return this.name;
    }

    //----------------------------------------------
    public final int getTriggerInputNumber() {
        return this.number;
    }

    //----------------------------------------------
    public int getGain() {
        return this.gain;
    }

    //----------------------------------------------
    public int getVelocityCurve() {
        return this.velocity;
    }

    //----------------------------------------------
    public int getThreshold() {
        return this.threshold;
    }

    //----------------------------------------------
    public int getXTalk() {
        return this.xTalk;
    }

    //----------------------------------------------
    public int getRetrigger() {
        return this.retrigger;
    }

    //----------------------------------------------
    public int getTriggerType() {
        return this.type;
    }

    //----------------------------------------------
    protected final void setTriggerInputNumber(int triggerInputNumber) {
        this.number = triggerInputNumber;
        Common.logger.log(Level.FINEST, "triggerInputNumber = <{0}>", this.number);
    }

    //----------------------------------------------
    public final void setTriggerInputName(String triggerInputName) {
        this.name = triggerInputName;
        Common.logger.log(Level.FINEST, "triggerInputName = <{0}>", this.name);
    }

    //----------------------------------------------
    public final void setGain(int gain) {
        this.gain = gain;
        Common.logger.log(Level.FINEST, "gain = <{0}>", this.gain);
    }

    //----------------------------------------------
    public final void setVelocityCurve(int velocityCurve) {
        this.velocity = velocityCurve;
        Common.logger.log(Level.FINEST, "velocityCurve = <{0}>", this.velocity);
    }

    //----------------------------------------------
    public final void setThreshold(int threshold) {
        this.threshold = threshold;
        Common.logger.log(Level.FINEST, "threshold = <{0}>", this.threshold);
    }

    //----------------------------------------------
    public final void setXTalk(int xTalk) {
        this.xTalk = xTalk;
        Common.logger.log(Level.FINEST, "xTalk = <{0}>", this.xTalk);
    }

    //----------------------------------------------
    public final void setRetrigger(int retrigger) {
        this.retrigger = retrigger;
        Common.logger.log(Level.FINEST, "retrigger = <{0}>", this.retrigger);
    }

    //----------------------------------------------
    public final void setTriggerType(int triggerType) {
        this.type = triggerType;
        Common.logger.log(Level.FINEST, "triggerType = <{0}>", this.type);
    }

    //----------------------------------------------
    private static void BuildLov() {
        // build LOV gain
        for (int i = 1; i <= 20; i++) {
            lovGain.put(i, Integer.toString(i));
        }

        // build LOV velocity curve
        lovVelocity.put(1, "off");
        lovVelocity.put(2, "ep4");
        lovVelocity.put(3, "ep3");
        lovVelocity.put(4, "ep2");
        lovVelocity.put(5, "ep1");
        lovVelocity.put(6, "lin");
        lovVelocity.put(7, "lg1");
        lovVelocity.put(8, "lg2");
        lovVelocity.put(9, "lg3");
        lovVelocity.put(10, "lg4");
        lovVelocity.put(11, "sp1");
        lovVelocity.put(12, "sp2");
        lovVelocity.put(13, "sp3");
        lovVelocity.put(14, "sp4");
        lovVelocity.put(15, "cst");

        // build LOV threshold
        for (int i = 3; i <= 64; i++) {
            lovThreshold.put(i, Integer.toString(i));
        }

        // build LOV xtalk
        for (int i = 0; i <= 20; i++) {
            lovXTalk.put(i, Integer.toString(i));
        }

        // build LOV retrigger
        for (int i = 0; i <= 127; i++) {
            lovRetrigger.put(i, Integer.toString(i));
        }

        // build LOV trigger type
        lovType.put(0, "pp");
        lovType.put(1, "ss");
        lovType.put(2, "ps");
        lovType.put(3, "sp");
        lovType.put(4, "sus");
        lovType.put(5, "hh");
    }
}
