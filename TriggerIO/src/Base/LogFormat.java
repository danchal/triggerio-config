/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Base;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 *
 * @author DrumTrigger
 */
public class LogFormat extends Formatter {

    private static final DateFormat format = new SimpleDateFormat("h:mm:ss");
    private static final String lineSep = System.getProperty("line.separator");

    /**
     * A Custom format implementation that is designed for brevity.
     */
    public LogFormat(){
        super();
    }
    
    public String format(LogRecord record) {        
        StringBuilder output = new StringBuilder().append("[").append(record.getLevel())
                .append('|').append(record.getSourceClassName()).append(".")
                .append(record.getSourceMethodName()).append("]: ")
                .append(formatMessage(record)).append(' ').append(lineSep);
        return output.toString();
    }
}

