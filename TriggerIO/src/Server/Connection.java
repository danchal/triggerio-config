package Server;

import Base.Common;
import Midi.DeviceMidi;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;

public class Connection implements Runnable{

    private Socket clientSocket = null;
    private ObjectInputStream ois = null;
    private ObjectOutputStream oos = null;

    private DeviceMidi device;

    public Connection(Socket socket, DeviceMidi device) {
        this.clientSocket = socket;
        this.device = device;
    }

    @Override
    public void run() {
        Common.logger.log(Level.INFO, "begin");

        try {
            ois = new ObjectInputStream(clientSocket.getInputStream());
            oos = new ObjectOutputStream(clientSocket.getOutputStream());

            boolean closeThread = false;

            try {
                while (!closeThread){
                    Document inputDoc;

                    Common.logger.log(Level.INFO, "waiting for request");

                    inputDoc = (Document) ois.readObject();

                    Common.logger.log(Level.INFO, "object received");

                    Protocol protocol = new Protocol(inputDoc, device);

                    Common.logger.log(Level.INFO, "response={0}", Tools.toString(protocol.outDocument));

                    oos.writeObject(protocol.outDocument);
                    oos.flush();

                    Common.logger.log(Level.INFO, "response sent");

                    switch (protocol.status){
                        case CLOSE :
                            closeThread = true;
                            break;
                        case OK :
                            break;
                        default :
                            break;
                    }
                }
            }catch (ParserConfigurationException ex) {
                Common.logger.log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Common.logger.log(Level.SEVERE, null, ex);
            }

            ois.close();
            oos.close();
            clientSocket.close();
            Common.logger.log(Level.INFO, "Connection closed");
        }
        catch (IOException ex) {
            Common.logger.log(Level.SEVERE, null, ex);
        }
    }
}