/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Base;

import Midi.Kit;
import Midi.Input;

/**
 *
 * @author dan
 */
public class Device {
    public static final int COUNTKIT = 21;
    public static final int COUNTINPUT = 21;
    public Kit[] kits = new Kit[COUNTKIT];
    public Input[] triggerInputs = new Input[COUNTINPUT];
        
    public Device(){        
    }    
}
