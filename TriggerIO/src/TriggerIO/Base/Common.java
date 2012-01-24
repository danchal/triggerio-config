/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TriggerIO.Base;

import java.io.IOException;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;

/**
 *
 * @author dan
 */
public class Common {

    public static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Common.class.getName());

    //---------------------------------------------------------------------
    static {
        try {
            Handler handler = new FileHandler("TriggerIO.log");
            handler.setFormatter(new LogFormat());
            logger.addHandler(handler);

            logger.setLevel(Level.INFO);

        } catch (IOException ex) {
            Common.logger.severe(ex.getLocalizedMessage());
        } catch (SecurityException ex) {
            Common.logger.severe(ex.getLocalizedMessage());
        }
    }

    //---------------------------------------------------------------------
    public static void setLogLevel(Level level) {
        Common.logger.setLevel(level);
        Common.logger.log(Level.INFO, "Log Level <{0}>", level.toString());

    }

    //-----------------------------------------------------------
    public static int getKey(Object value, Map<Integer, String> map) {
        Common.logger.log(Level.FINEST, "value = <{0}>", value);

        int key = -1;

        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(value.toString())) {
                key = entry.getKey();
            }
        }
        Common.logger.log(Level.FINEST, "return = <{0}>", key);
        return key;
    }
}
