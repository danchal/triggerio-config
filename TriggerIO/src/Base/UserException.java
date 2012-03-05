package Base;

import java.util.logging.Level;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author DrumTrigger@gmail.com
 */
public class UserException extends Exception {

    public UserException(String message) {
        super(message);
        Common.logger.log(Level.SEVERE, message);
    }
}
