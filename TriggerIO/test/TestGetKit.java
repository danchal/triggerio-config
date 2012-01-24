
import TriggerIO.Midi.Common;
import TriggerIO.Midi.DeviceMidi;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

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

        DeviceMidi device = new DeviceMidi();
        try {
            device.importXml(new File("/home/dan/Downloads/test.tct"));
        } catch (Exception ex) {
            Logger.getLogger(TestGetKit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

