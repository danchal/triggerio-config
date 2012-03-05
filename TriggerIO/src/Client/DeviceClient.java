/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import Base.*;
import Server.Protocol;
import Server.Tools;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author dan
 */
public class DeviceClient extends Device {

    public enum Parameter {
        KIT, INPUT, PROG_CHANGE, MIDI_CHANNEL, MIDI_NOTE, GAIN, VEL_CURVE, THRESHOLD, XTALK, RETRIGGER, TRIGGER_TYPE
    }
    private int currentKitNumber;
    private int currentInputNumber;

    private Socket serverSocket = null;
    private ObjectOutputStream oos = null;
    private ObjectInputStream ois = null;
    private String serverAddress;
    private int serverPort;

    public DeviceClient(){
        super();
    }

    public DeviceClient(int kitNumber, int inputNumber, String serverAddress, int serverPort) {
        super();
        this.currentKitNumber = kitNumber;
        this.currentInputNumber = inputNumber;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public int getCurrentInputNumber() {
        return currentInputNumber;
    }

    public void setCurrentInputNumber(int currentInputNumber) {
        this.currentInputNumber = currentInputNumber;
    }

    public int getCurrentKitNumber() {
        return currentKitNumber;
    }

    public void setCurrentKitNumber(int currentKitNumber) {
        this.currentKitNumber = currentKitNumber;
    }

    // --------------------------------------------
    public int getkey(Parameter parameter) {
        Common.logger.log(Level.FINE, "parameter=<{0}>", new Object[]{parameter});
        int key = -99;

        switch (parameter) {
            case KIT:
                key = currentKitNumber;
                break;
            case INPUT:
                key = currentInputNumber;
                break;
            case PROG_CHANGE:
                key = getKit(currentKitNumber).getProgramChange();
                break;
            case MIDI_CHANNEL:
                key = getKit(currentKitNumber).inputs.get(currentInputNumber).getChannel();
                break;
            case MIDI_NOTE:
                key = getKit(currentKitNumber).inputs.get(currentInputNumber).getNote();
                break;
            case GAIN:
                key = getGlobalInput(currentInputNumber).getGain();
                break;
            case RETRIGGER:
                key = getGlobalInput(currentInputNumber).getRetrigger();
                break;
            case THRESHOLD:
                key = getGlobalInput(currentInputNumber).getThreshold();
                break;
            case TRIGGER_TYPE:
                key = getGlobalInput(currentInputNumber).getTriggerType();
                break;
            case VEL_CURVE:
                key = getGlobalInput(currentInputNumber).getVelocityCurve();
                break;
            case XTALK:
                key = getGlobalInput(currentInputNumber).getXTalk();
                break;
        }
        Common.logger.log(Level.FINE, "return key=<{0}>", new Object[]{key});
        return key;
    }

    // -----------------------------------------
    public String getValue(Parameter parameter) {
        Common.logger.log(Level.FINE, "parameter=<{0}>", new Object[]{parameter});
        String value = null;

        switch (parameter) {
            case KIT:
                value = getKit(currentKitNumber).getKitName();
                break;
            case INPUT:
                value = getGlobalInput(currentInputNumber).getName();
                break;
            case PROG_CHANGE:
                value = Kit.lovProgramChange.get(getkey(parameter));
                break;
            case MIDI_CHANNEL:
                value = Input.lovChannel.get(getkey(parameter));
                break;
            case MIDI_NOTE:
                value = Input.lovNote.get(getkey(parameter));
                break;
            case GAIN:
                value = GlobalInput.lovGain.get(getkey(parameter));
                break;
            case RETRIGGER:
                value = GlobalInput.lovRetrigger.get(getkey(parameter));
                break;
            case THRESHOLD:
                value = GlobalInput.lovThreshold.get(getkey(parameter));
                break;
            case TRIGGER_TYPE:
                value = GlobalInput.lovType.get(getkey(parameter));
                break;
            case VEL_CURVE:
                value = GlobalInput.lovVelocity.get(getkey(parameter));
                break;
            case XTALK:
                value = GlobalInput.lovXTalk.get(getkey(parameter));
                break;
            default:
                break;
        }
        Common.logger.log(Level.FINE, "return value=<{0}>", new Object[]{value});
        return value;
    }

    // ----------------------------------------------
    public boolean sendKey(Parameter parameter, int newKey) {
        int originalKey = getkey(parameter);
        boolean changed = setKey(parameter, newKey);
        boolean sent = false;

        if (changed) {
            try {
                sent = sendMessage(Protocol.ACTION_UPDATE, parameter);
            } catch (UserException ex) {
                Common.logger.log(Level.SEVERE, ex.toString());
            }

            if (!sent) {
                Common.logger.log(Level.INFO, "unable to send message, revert to original key");
                setKey(parameter, originalKey);
            }
        }
        Common.logger.log(Level.FINE, "return sent=<{0}>", new Object[]{String.valueOf(sent)});
        return sent;
    }

    // ----------------------------------------------
    private boolean setKey(Parameter parameter, int key) {
        Common.logger.log(Level.FINE, "parameter=<{0}>, key=<{1}>", new Object[]{parameter, key});
        boolean changed = false;

        if (isConnected()) {

            switch (parameter) {
                case KIT:
                    if (kitExists(key)) {
                        currentKitNumber = key;
                        changed = true;
                    }
                    break;
                case INPUT:
                    if (globalInputExists(key)) {
                        currentInputNumber = key;
                        changed = true;
                    }
                    break;
                case PROG_CHANGE:
                    if (Kit.lovProgramChange.containsKey(key)) {
                        getKit(currentKitNumber).setProgramChange(key);
                        changed = true;
                    }
                    break;
                case MIDI_CHANNEL:
                    if (Input.lovChannel.containsKey(key)) {
                        getKit(currentKitNumber).inputs.get(currentInputNumber).setChannel(key);
                        changed = true;
                    }
                    break;
                case MIDI_NOTE:
                    if (Input.lovNote.containsKey(key)) {
                        getKit(currentKitNumber).inputs.get(currentInputNumber).setNote(key);
                        changed = true;
                    }
                    break;
                case GAIN:
                    if (GlobalInput.lovGain.containsKey(key)) {
                        globalInputs.get(currentInputNumber).setGain(key);
                        changed = true;
                    }
                    break;
                case RETRIGGER:
                    if (GlobalInput.lovRetrigger.containsKey(key)) {
                        globalInputs.get(currentInputNumber).setRetrigger(key);
                        changed = true;
                    }
                    break;
                case THRESHOLD:
                    if (GlobalInput.lovThreshold.containsKey(key)) {
                        globalInputs.get(currentInputNumber).setThreshold(key);
                        changed = true;
                    }
                    break;
                case TRIGGER_TYPE:
                    if (GlobalInput.lovType.containsKey(key)) {
                        globalInputs.get(currentInputNumber).setTriggerType(key);
                        changed = true;
                    }
                    break;
                case VEL_CURVE:
                    if (GlobalInput.lovVelocity.containsKey(key)) {
                        globalInputs.get(currentInputNumber).setVelocityCurve(key);
                        changed = true;
                    }
                    break;
                case XTALK:
                    if (GlobalInput.lovXTalk.containsKey(key)) {
                        globalInputs.get(currentInputNumber).setXTalk(key);
                        changed = true;
                    }
                    break;
                default:
                    break;
            }
        }
        Common.logger.log(Level.FINE, "return changed =<{0}>", new Object[]{String.valueOf(changed)});
        return changed;
    }

    // -----------------------------------------
    private Document createRequest(String actionType, boolean sendGlobalInput) throws UserException {
        Common.logger.log(Level.FINE, "createRequest");
        Document request = createRequest(actionType);

        Element rootElement = request.getDocumentElement();
        Element device;

        if (sendGlobalInput) {
            device = get(request, currentInputNumber);
        } else {
            device = get(request, currentKitNumber, currentInputNumber);
        }

        rootElement.appendChild(device);

        Common.logger.log(Level.FINE, "return request={0}", Tools.toString(request));
        return request;
    }

    //-----------------------------------------
    private Document createRequest(String actionType) throws UserException {
        Common.logger.log(Level.INFO, "actionType={0}", actionType);
        Document request = Tools.createDocument(actionType);
        return request;
    }

    // -----------------------------------------
    private boolean sendMessage(String actionType, Parameter parameter) throws UserException {
        boolean sendGlobalInput = true;

        switch (parameter) {
            case KIT :
                sendGlobalInput = false;
                break;
            case INPUT :
                sendGlobalInput = false;
                break;
            case PROG_CHANGE :
                sendGlobalInput = false;
                break;
            case MIDI_CHANNEL :
                sendGlobalInput = false;
                break;
            case MIDI_NOTE :
                sendGlobalInput = false;
                break;
            default:
                break;
        }
        Common.logger.log(Level.FINE, "sendGlobalInput={0}", String.valueOf(sendGlobalInput));
        return sendMessage(createRequest(actionType, sendGlobalInput));
    }

    // -----------------------------------------
    private boolean sendMessage(Document request) throws UserException {
        Common.logger.log(Level.FINE, "sendMessage");
        Common.logger.log(Level.FINE, Tools.toString(request));
        boolean success = false;

        Document response;

        Common.logger.log(Level.FINE, "write the objects to the server");
        try {
            oos.writeObject(Tools.toString(request));
            oos.flush();
        }  catch (IOException ex) {
            throw new UserException("unable to write to server");
        }

        Common.logger.log(Level.FINE, "waiting for response");
        try {
            response = Tools.toXml((String) ois.readObject());
        } catch (SocketTimeoutException ex) {
            throw new UserException("SocketTimeoutException");
        } catch (IOException ex) {
            throw new UserException("IOException");
        } catch (ClassNotFoundException ex) {
            throw new UserException(ex.toString());
        }

        Common.logger.log(Level.FINE, "object received");
        Common.logger.log(Level.FINE, "response=<{0}>", Tools.toString(response));

        String responseActionType;
        Element rootElement;

        {
            NodeList nodeList = response.getElementsByTagName(Protocol.ROOT);
            rootElement = (Element) nodeList.item(0);
        }

        responseActionType = rootElement.getAttribute(Protocol.ACTIONTYPE);
        Common.logger.log(Level.FINE, "responseActionType=<{0}>", responseActionType);

        if (Protocol.ACTION_OK.equalsIgnoreCase(responseActionType)) {
            success = true;
        } else if (Protocol.ACTION_SET.equalsIgnoreCase(responseActionType)) {
            NodeList nodeList = rootElement.getElementsByTagName(Device.ROOT);
            Element deviceElement = (Element) nodeList.item(0);

            set(deviceElement);
            success = true;

        } else {
            Common.logger.log(Level.SEVERE, "FAIL");
        }
        Common.logger.log(Level.FINE, "success=<{0}>", String.valueOf(success));
        return success;
    }

    // --------------------------------------------------
    public boolean ping() throws UserException{
        return sendMessage(createRequest(Protocol.ACTION_PING));
    }

    // --------------------------------------------------
    public boolean isAlive(){
        boolean isAlive = false;
        try {
            if (isConnected()) {
                isAlive = ping();
            }
        } catch (NullPointerException e) {
            Common.logger.log(Level.SEVERE, "Socket does not exist");
        } catch (UserException ex){
        }
        Common.logger.log(Level.FINE, "connected=<{0}>", String.valueOf(isAlive));
        return isAlive;
    }

    // --------------------------------------------------
    public boolean isConnected(){
        boolean isConnected = false;
        try {
            isConnected = serverSocket.isConnected() & !serverSocket.isClosed();
        } catch (NullPointerException e) {
            Common.logger.log(Level.SEVERE, "Socket does not exist");
        }
        Common.logger.log(Level.FINE, "connected=<{0}>", String.valueOf(isConnected));
        return isConnected;
    }

    // --------------------------------------
    public boolean connect() {
        Common.logger.log(Level.FINE, "serverAddress=<{0}>, serverPort=<{1}>", new Object[] {serverAddress, serverPort});
        boolean success = false;

        if (isConnected()) {
            try {
                serverSocket.close();
                Common.logger.log(Level.SEVERE, "socket has been closed");
            } catch (IOException ex) {
                Common.logger.log(Level.SEVERE, "unable to close socket");
            }
        }

        Common.logger.log(Level.INFO, "Attempting to create new connection");
        try {
            serverSocket = new Socket(serverAddress, serverPort);
            serverSocket.setSoTimeout(2000);

            Common.logger.log(Level.FINE, "before io streams");
            oos = new ObjectOutputStream(serverSocket.getOutputStream());
            ois = new ObjectInputStream(serverSocket.getInputStream());

            Common.logger.log(Level.FINE, "before send message");
            success = sendMessage(createRequest(Protocol.ACTION_GET));

        } catch (UserException ex) {
            Common.logger.log(Level.SEVERE, ex.toString());
        } catch (UnknownHostException e) {
            Common.logger.log(Level.SEVERE, "Don't know about host");
        } catch (IOException e) {
            Common.logger.log(Level.SEVERE, "Couldn't get I/O for the connection");
        }
        return (success & isConnected());
    }
}