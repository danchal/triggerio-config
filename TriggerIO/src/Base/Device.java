/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Base;

import org.w3c.dom.Element;

/**
 *
 * @author dan
 */
public class Device extends DeviceAbstract<Kit, GlobalInput> {

    @Override
    protected void addKit(Element element, int i) {
        kits.add(i, new Kit(element));
    }

    @Override
    protected void addGlobalInput(Element element, int i) {
        globalInputs.add(i, new GlobalInput(element));
    }

}
