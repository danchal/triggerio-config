/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Base;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 *
 * @author dan
 */
public class Input {
    private String triggerInputName;
    private int triggerInputNumber;
    private int gain;
    private int velocityCurve;
    private int threshold;
    private int xTalk;
    private int retrigger;
    private int triggerType;

    public static final Map<Integer, String> lovGain = new LinkedHashMap<Integer, String>();
    public static final Map<Integer, String> lovVelocityCurve = new LinkedHashMap<Integer, String>();
    public static final Map<Integer, String> lovThreshold = new LinkedHashMap<Integer, String>();
    public static final Map<Integer, String> lovRetrigger = new LinkedHashMap<Integer, String>();
    public static final Map<Integer, String> lovXTalk = new LinkedHashMap<Integer, String>();
    public static final Map<Integer, String> lovTriggerType = new LinkedHashMap<Integer, String>();

         static {
        // build LOV gain
        for (int i = 1; i <= 20; i++) {
            lovGain.put(i, Integer.toString(i));
        }

        // build LOV velocity curve
        lovVelocityCurve.put(1, "off");
        lovVelocityCurve.put(2, "ep4");
        lovVelocityCurve.put(3, "ep3");
        lovVelocityCurve.put(4, "ep2");
        lovVelocityCurve.put(5, "ep1");
        lovVelocityCurve.put(6, "lin");
        lovVelocityCurve.put(7, "lg1");
        lovVelocityCurve.put(8, "lg2");
        lovVelocityCurve.put(9, "lg3");
        lovVelocityCurve.put(10, "lg4");
        lovVelocityCurve.put(11, "sp1");
        lovVelocityCurve.put(12, "sp2");
        lovVelocityCurve.put(13, "sp3");
        lovVelocityCurve.put(14, "sp4");
        lovVelocityCurve.put(15, "cst");

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
        lovTriggerType.put(0, "pp");
        lovTriggerType.put(1, "ss");
        lovTriggerType.put(2, "ps");
        lovTriggerType.put(3, "sp");
        lovTriggerType.put(4, "sus");
        lovTriggerType.put(5, "hh");
    }
    
    protected Input(){
    }
    
    //----------------------------------------------
    public Input(int triggerInputNumber, String triggerInputName,
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

    public final String getInputName(int  inputNumber){
        return ( "Input-" + String.valueOf( (inputNumber/2) + 1) + (inputNumber%2==0 ? "T" : "R"));
    }    
    
    //----------------------------------------------
    public final String getTriggerInputName() {
        return this.triggerInputName;
    }

    //----------------------------------------------
    public final int getTriggerInputNumber() {
        return this.triggerInputNumber;
    }

    //----------------------------------------------
    public int getGain() {
        return this.gain;
    }

    //----------------------------------------------
    public int getVelocityCurve() {
        return this.velocityCurve;
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
        return this.triggerType;
    }

    //----------------------------------------------
    protected final void setTriggerInputNumber(int triggerInputNumber) {
        this.triggerInputNumber = triggerInputNumber;
        Common.logger.log(Level.FINEST, "triggerInputNumber = <{0}>", this.triggerInputNumber);
    }

    //----------------------------------------------
    public final void setTriggerInputName(String triggerInputName) {
        this.triggerInputName = triggerInputName;
        Common.logger.log(Level.FINEST, "triggerInputName = <{0}>", this.triggerInputName);
    }

    //----------------------------------------------
    public final void setGain(int gain) {
        this.gain = gain;
        Common.logger.log(Level.FINEST, "gain = <{0}>", this.gain);
    }

    //----------------------------------------------
    public final void setVelocityCurve(int velocityCurve) {
        this.velocityCurve = velocityCurve;
        Common.logger.log(Level.FINEST, "velocityCurve = <{0}>", this.velocityCurve);
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
        this.triggerType = triggerType;
        Common.logger.log(Level.FINEST, "triggerType = <{0}>", this.triggerType);
    }    
}
