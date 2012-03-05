/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

/**
 *
 * @author dan
 */
public class ThreadMessage {
    private boolean status;

    public ThreadMessage(boolean status) {
        this.status = status;
    }

    public boolean getRefresh(){
        return status;
    }

    public void setRefresh(boolean in){
        status = in;
    }
}
