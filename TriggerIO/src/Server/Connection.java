package Server;

import Base.Common;
import Base.UserException;
import Midi.DeviceMidi;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import org.w3c.dom.Document;

public class Connection implements Runnable {

    private Socket clientSocket = null;
    private DeviceMidi device;
    private ThreadMessage refresh;

    public Connection(Socket socket, DeviceMidi device, ThreadMessage refresh) {
        this.clientSocket = socket;
        this.device = device;
        this.refresh = refresh;
    }

    @Override
    public void run() {
        Common.logger.log(Level.INFO, "begin");

        ObjectInputStream ois;
        ObjectOutputStream oos;

        try {
            clientSocket.setSoTimeout(1000);
            ois = new ObjectInputStream(clientSocket.getInputStream());
            oos = new ObjectOutputStream(clientSocket.getOutputStream());

            boolean closeThread = false;
            try {
                while (!closeThread) {
                    Document request = null;

                    Common.logger.log(Level.INFO, "waiting for request");
                    {
                        boolean loop = true;
                        while (loop) {
                            try {
                                request = Tools.toXml((String) ois.readObject());
                                loop = false;
                            } catch (SocketTimeoutException ex) {
                                if (Thread.interrupted()) {
                                    Common.logger.log(Level.INFO, "Interrupted whilst waiting for request");
                                    throw new InterruptedException("waiting for request");
                                }
                            }
                        }
                    }

                    Common.logger.log(Level.INFO, "object received");
                    Common.logger.log(Level.INFO, "request={0}", Tools.toString(request));
                    refresh.setRefresh(true);

                    Protocol protocol = new Protocol(request, device);
                    Common.logger.log(Level.INFO, "attempting to send response={0}", Tools.toString(protocol.response));
                    oos.writeObject(Tools.toString(protocol.response));
                    oos.flush();
                    Common.logger.log(Level.INFO, "response sent");

                    switch (protocol.status) {
                        case CLOSE:
                            closeThread = true;
                            break;
                        case OK:
                            break;
                        default:
                            break;
                    }
                }
            } catch (UserException ex) {
                Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidMidiDataException ex) {
                Common.logger.log(Level.SEVERE, "InvalidMidiDataException", ex);
            } catch (MidiUnavailableException ex) {
                Common.logger.log(Level.SEVERE, "MidiUnavailableException", ex);
            } catch (ClassNotFoundException ex) {
                Common.logger.log(Level.SEVERE, "ClassNotFoundException", ex);
            } catch (IOException ex) {
                Common.logger.log(Level.SEVERE, "IOException", ex);
            } catch (InterruptedException ex) {
            }

            ois.close();
            oos.close();
            clientSocket.close();
        } catch (IOException ex) {
            Common.logger.log(Level.SEVERE, "IOException getting sockets", ex);
        }
        Common.logger.log(Level.INFO, "Connection closed");
    }
}