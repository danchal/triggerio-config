/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Base.Common;
import Midi.DeviceMidi;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dan
 */
public class Server implements Runnable {

    private int serverPort;
    private ServerSocket serverSocket = null;
    private DeviceMidi device;

    public Server(int inServerPort, DeviceMidi device) {
        this.serverPort = inServerPort;
        this.device = device;
    }

    @Override
    public void run() {
        Common.logger.log(Level.INFO, "Starting server");

        try {
            serverSocket = new ServerSocket(serverPort);
            serverSocket.setSoTimeout(1000);


            Common.logger.log(Level.INFO, "Waiting for connections.");
            Socket clientSocket = null;

            boolean loop = true;

            while (loop){
                try {

                    clientSocket = serverSocket.accept();
                    loop = false;

                } catch (SocketTimeoutException ex){
                    if (Thread.interrupted()){
                        Common.logger.log(Level.INFO, "Interrupted whilst waiting for connection");
                        throw new InterruptedException();
                    }
                }
            }

            Common.logger.log(Level.INFO, "Accepted a connection from: {0}", clientSocket.getInetAddress());

            Thread connection = new Thread(new Connection(clientSocket, device));

            connection.start();

            connection.join();

            closeServer();

        }catch (IOException ex) {
            Common.logger.log(Level.SEVERE, "Could not listen on port: {0}", serverPort);
        } catch (InterruptedException ex){
            closeServer();
        }
        Common.logger.log(Level.INFO, "Server closed <{0}>", serverSocket.isClosed());
    }

    public void closeServer() {
        try {
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public InetAddress getServerAddress() {
        return serverSocket.getInetAddress();
    }

    public int getServerPort() {
        return serverPort;
    }

    public boolean isServerRunning() {
        return serverSocket.isBound();
    }
}
