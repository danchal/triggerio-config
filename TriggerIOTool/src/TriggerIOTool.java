
import Base.Input;
import Midi.*;
import Server.Server;
import components.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.*;
import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author DrumTrigger
 */
public class TriggerIOTool extends JFrame {

    //-----------------------------------------------------------
    public static void main(String[] args) {
        boolean debug = false;

        if (args.length != 0) {
            if (args[0].equalsIgnoreCase("debug")) {
                Common.setLogLevel(Level.ALL);
                debug = true;
            }
        }
        TriggerIOTool triggerIOTool = new TriggerIOTool(debug);
    }

    //-----------------------------------------------------------
    public TriggerIOTool(boolean debug) {
        initDevice();
        initLov();
        initLabels();
        initTables();
        addTableListeners();
        initMenu();
        initPanel();
        initFrame();
        addWindowListeners();

        transferOptionsDialog = new TransferOptionsDialog(this, true);
        transferOptionsDialog.pack();

        fileStatus(false);
        goOffline();

        Common.logger.log(Level.INFO, "Application.version = <{0}>", bundleVersion.getString("Application.version"));
        Common.logger.log(Level.INFO, "Application.revision = <{0}>", bundleVersion.getString("Application.revision"));
        Common.logger.log(Level.INFO, "Application.build = <{0}>", bundleVersion.getString("Application.build"));

        Common.logger.log(Level.FINE, "bundleVersion.getString(File.xml) <{0}>", bundleVersion.getString("File.xml"));
        Common.logger.log(Level.FINE, "bundleVersion.getString(File.sysex) <{0}>", bundleVersion.getString("File.sysex"));

        if (debug) {
            currentFile = new File("default.syx");
            openFile();
        }
    }

    //-----------------------------------------------------------
    private void initDevice() {
        triggerIODevice = new DeviceMidi();
    }

    //-----------------------------------------------------------
    private void initFrame() {

        this.add(panelMain);
        this.setSize(800, 500);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setTitle(bundleVersion.getString("Application.title"));

        this.setVisible(true);
    }

    //-----------------------------------------------------------
    private void initLabels() {
        labelFileName = new JLabel("---");
        labelDeviceName = new JLabel(OFFLINE);
    }

    //-----------------------------------------------------------
    private void initPanel() {
        tabbedPaneMain = new JTabbedPane();
        panelProgramChange = new JPanel();
        panelMidiChannel = new JPanel();
        panelMidiNote = new JPanel();
        panelTriggerInput = new JPanel();

        panelMain = new JPanel(new BorderLayout());
        panelButtons = new JPanel(new GridBagLayout());

        panelProgramChange.setLayout(new GridLayout());
        panelMidiChannel.setLayout(new GridLayout());
        panelMidiNote.setLayout(new GridLayout());
        panelTriggerInput.setLayout(new GridLayout());

        tabbedPaneMain.addTab("Program Change", panelProgramChange);
        tabbedPaneMain.addTab("Midi Channel", panelMidiChannel);
        tabbedPaneMain.addTab("Midi Note", panelMidiNote);
        tabbedPaneMain.addTab("Trigger Input", panelTriggerInput);

        JScrollPane scrollPaneProgramChange = new JScrollPane(tableProgramChange);
        JScrollPane scrollPaneMidiChannel = new JScrollPane(tableMidiChannel);
        JScrollPane scrollPaneMidiNote = new JScrollPane(tableMidiNote);
        JScrollPane scrollPaneTriggerInput = new JScrollPane(tableTriggerInput);

        //Add the scroll pane to this panel.
        panelProgramChange.add(scrollPaneProgramChange);
        panelMidiChannel.add(scrollPaneMidiChannel);
        panelMidiNote.add(scrollPaneMidiNote);
        panelTriggerInput.add(scrollPaneTriggerInput);

        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.anchor = GridBagConstraints.LINE_START;
        panelButtons.add(Box.createRigidArea(new Dimension(0, 10)), c);

        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0;
        c.anchor = GridBagConstraints.LINE_START;
        panelButtons.add(new JLabel("Current file"), c);

        c.fill = GridBagConstraints.NONE;
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0;
        c.anchor = GridBagConstraints.LINE_START;
        panelButtons.add(new JLabel("  :  "), c);


        c.fill = GridBagConstraints.NONE;
        c.gridx = 2;
        c.gridy = 1;
        c.weightx = 1;
        c.anchor = GridBagConstraints.LINE_START;
        panelButtons.add(labelFileName, c);

        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0;
        c.anchor = GridBagConstraints.LINE_START;
        panelButtons.add(new JLabel("Current device"), c);

        c.fill = GridBagConstraints.NONE;
        c.gridx = 1;
        c.gridy = 2;
        c.weightx = 0;
        c.anchor = GridBagConstraints.LINE_START;
        panelButtons.add(new JLabel("  :  "), c);

        c.fill = GridBagConstraints.NONE;
        c.gridx = 2;
        c.gridy = 2;
        c.weightx = 1;
        c.anchor = GridBagConstraints.LINE_START;
        panelButtons.add(labelDeviceName, c);

        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 0;
        c.anchor = GridBagConstraints.LINE_START;
        panelButtons.add(Box.createRigidArea(new Dimension(0, 10)), c);

        panelMain.add(panelButtons, BorderLayout.PAGE_START);
        panelMain.add(tabbedPaneMain, BorderLayout.CENTER);
    }

    //-----------------------------------------------------------
    private void initLov() {

        Common.logger.log(Level.FINEST, "count of lovProgramChange=<{0}", String.valueOf(KitMidi.lovProgramChange.size() + ">"));
        Common.logger.log(Level.FINEST, "count of lovTriggerMidiChannel=<{0}", String.valueOf(Input.lovChannel.size() + ">"));
        Common.logger.log(Level.FINEST, "count of lovTriggerMidiNote=<{0}", String.valueOf(Input.lovNote.size() + ">"));

        Common.logger.log(Level.FINEST, "count of lovGain=<{0}", String.valueOf(GlobalInputMidi.lovGain.size() + ">"));
        Common.logger.log(Level.FINEST, "count of lovRetrigger=<{0}", String.valueOf(GlobalInputMidi.lovRetrigger.size() + ">"));
        Common.logger.log(Level.FINEST, "count of lovThreshold=<{0}", String.valueOf(GlobalInputMidi.lovThreshold.size() + ">"));
        Common.logger.log(Level.FINEST, "count of lovTriggerType=<{0}", String.valueOf(GlobalInputMidi.lovType.size() + ">"));
        Common.logger.log(Level.FINEST, "count of lovVelocityCurve=<{0}", String.valueOf(GlobalInputMidi.lovVelocity.size() + ">"));
        Common.logger.log(Level.FINEST, "count of lovXTalk=<{0}", String.valueOf(GlobalInputMidi.lovXTalk.size() + ">"));

        lovProgramChange = new JComboBox(KitMidi.lovProgramChange.values().toArray());
        lovTriggerMidiChannel = new JComboBox(Input.lovChannel.values().toArray());
        lovTriggerMidiNote = new JComboBox(Input.lovNote.values().toArray());

        lovGain = new JComboBox(GlobalInputMidi.lovGain.values().toArray());
        lovRetrigger = new JComboBox(GlobalInputMidi.lovRetrigger.values().toArray());
        lovThreshold = new JComboBox(GlobalInputMidi.lovThreshold.values().toArray());
        lovTriggerType = new JComboBox(GlobalInputMidi.lovType.values().toArray());
        lovVelocityCurve = new JComboBox(GlobalInputMidi.lovVelocity.values().toArray());
        lovXTalk = new JComboBox(GlobalInputMidi.lovXTalk.values().toArray());

        lovProgramChange.setMaximumRowCount(10);
    }

    //-----------------------------------------------------------
    private void initTables() {
        tableModelProgramChange = new TableModelProgramChange();
        tableModelMidiChannel = new TableModelMidiChannel();
        tableModelMidiNote = new TableModelMidiNote();
        tableModelTriggerInput = new TableModelTriggerInput();

        tableProgramChange = new JTable(tableModelProgramChange);
        tableMidiChannel = new JTable(tableModelMidiChannel);
        tableMidiNote = new JTable(tableModelMidiNote);
        tableTriggerInput = new JTable(tableModelTriggerInput);

        tableProgramChange.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableMidiChannel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableMidiNote.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableTriggerInput.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tableProgramChange.setColumnSelectionAllowed(true);

        tableProgramChange.setRowSelectionAllowed(true);
        tableMidiChannel.setRowSelectionAllowed(true);
        tableMidiNote.setRowSelectionAllowed(true);
        tableTriggerInput.setRowSelectionAllowed(true);

        Common.logger.log(Level.FINEST, "tableProgramChange.getColumnCount() ={0}", tableProgramChange.getColumnCount());

        for (int column = 0; column < tableProgramChange.getColumnCount(); column++) {
            tableProgramChange.getColumnModel().getColumn(column).setCellEditor(new DefaultCellEditor(lovProgramChange));
        }

        Common.logger.log(Level.FINEST, "tableMidiChannel.getColumnCount() ={0}", tableMidiChannel.getColumnCount());

        for (int column = 0; column < tableMidiChannel.getColumnCount(); column++) {
            tableMidiChannel.getColumnModel().getColumn(column).setCellEditor(new DefaultCellEditor(lovTriggerMidiChannel));
            tableMidiChannel.getColumnModel().getColumn(column).setCellRenderer(new DefaultTableCellRenderer());
        }

        Common.logger.log(Level.FINEST, "tableMidiNote.getColumnCount() ={0}", tableMidiNote.getColumnCount());

        for (int column = 0; column < tableMidiNote.getColumnCount(); column++) {
            tableMidiNote.getColumnModel().getColumn(column).setCellEditor(new DefaultCellEditor(lovTriggerMidiNote));
        }

        tableProgramChange.getColumnModel().getColumn(0).setMinWidth(110);
        tableMidiNote.getColumnModel().getColumn(0).setMinWidth(110);
        tableMidiChannel.getColumnModel().getColumn(0).setMinWidth(110);
        tableTriggerInput.getColumnModel().getColumn(0).setMinWidth(110);

        tableTriggerInput.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(lovGain));
        tableTriggerInput.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(lovVelocityCurve));
        tableTriggerInput.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(lovThreshold));
        tableTriggerInput.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(lovXTalk));
        tableTriggerInput.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(lovRetrigger));
        tableTriggerInput.getColumnModel().getColumn(6).setCellEditor(new DefaultCellEditor(lovTriggerType));

        tableProgramChange.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableMidiNote.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableMidiChannel.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableTriggerInput.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

    }

    //-----------------------------------------------------------
    private void setEnabledTables(boolean enabled) {
        tableProgramChange.setEnabled(enabled);
        tableMidiChannel.setEnabled(enabled);
        tableMidiNote.setEnabled(enabled);
        tableTriggerInput.setEnabled(enabled);
    }

    //-----------------------------------------------------------
    private void addTableListeners() {
        tableMidiNote.addKeyListener(new KeyListener() {

            public void keyTyped(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            public void keyPressed(KeyEvent e) {
                //keyPressedTableMidiChannel(e);
            }

            public void keyReleased(KeyEvent e) {
                keyListenerTableMidiNote(e);
            }
        });

        tableProgramChange.getModel().addTableModelListener(new TableModelListener() {

            public void tableChanged(TableModelEvent evt) {
                tableChangedProgramChange(evt);
            }
        });
        tableMidiChannel.getModel().addTableModelListener(new TableModelListener() {

            public void tableChanged(TableModelEvent evt) {
                tableChangedMidiChannel(evt);
            }
        });
        tableMidiNote.getModel().addTableModelListener(new TableModelListener() {

            public void tableChanged(TableModelEvent evt) {
                tableChangedMidiNote(evt);
            }
        });

        tableTriggerInput.getModel().addTableModelListener(new TableModelListener() {

            public void tableChanged(TableModelEvent evt) {
                tableChangedTriggerInput(evt);
            }
        });
    }

    //----------------------------------------------------------
    private void initMenu() {
        menuBarMain = new JMenuBar();
        initMenuFile();
        initMenuDevice();
        initMenuDownload();
        initMenuServer();
        initMenuHelp();

        menuBarMain.add(menuFile);
        menuBarMain.add(menuDevice);
        menuBarMain.add(menuDownload);
        //menuBarMain.add(menuServer);
        menuBarMain.add(menuHelp);
        this.setJMenuBar(menuBarMain);
    }

    //-----------------------------------------------------------
    private void initMenuDownload() {
        menuDownload = new JMenu("Download");

        menuDownload.addMenuListener(new MenuListener() {

            public void menuSelected(MenuEvent e) {
                menuSelectedDownload(e);
            }

            public void menuDeselected(MenuEvent e) {
                //
            }

            public void menuCanceled(MenuEvent e) {
                //
            }
        });
    }

    //-----------------------------------------------------------
    private void menuSelectedDownload(MenuEvent evt) {
        refreshTransmittingDevices();
        menuDownload.removeAll();

        boolean selectedDevice = false;
        for (MidiDevice.Info deviceInfo : transmittingDeviceInfos) {

            JMenuItem menuItem = new JMenuItem(deviceInfo.getName());
            menuItem.setActionCommand(getDeviceInfoHash(deviceInfo));
            menuItem.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    actionPerformedMenuItemDownload(evt);
                }
            });

            menuDownload.add(menuItem);
        }
    }

    //-----------------------------------------------------------
    private void initMenuHelp() {
        menuHelp = new JMenu("Help");
        menuItemHelpAbout = new JMenuItem("About");

        menuItemHelpAbout.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                actionPerformedMenuItemHelpAbout(evt);
            }
        });

        menuHelp.add(menuItemHelpAbout);
    }

    //-----------------------------------------------------------
    private String getApplicationVersion() {
        return (bundleVersion.getString("Application.version") + "."
                + bundleVersion.getString("Application.revision") + "."
                + bundleVersion.getString("Application.build"));
    }

    //-----------------------------------------------------------
    private void actionPerformedMenuItemHelpAbout(ActionEvent evt) {
        JOptionPane.showMessageDialog(this, bundleVersion.getString("Application.title")
                + "\nVersion: " + getApplicationVersion()
                + "\n"
                + "\n"
                + bundleVersion.getString("Author.web"));
    }

    //-----------------------------------------------------------
    private void menuSelectedServer() {
        setServerMenuStatus();
    }

    //-----------------------------------------------------------
    private void setServerMenuStatus() {
        Common.logger.fine("setServerMenuStatus");
        menuItemServerStart.setEnabled(!serverThread.isAlive());
        menuItemServerStop.setEnabled(serverThread.isAlive());
    }

    //-----------------------------------------------------------
    private void actionPerformedMenuItemServerStart(ActionEvent evt) {
        StartServerDialog dialog = new StartServerDialog(this, true, serverPort);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        int returnStatus = dialog.getReturnStatus();

        if (returnStatus == StartServerDialog.RET_OK) {
            serverPort = dialog.getServerPort();

            serverThread = new Thread(new Server(serverPort, triggerIODevice));
            serverThread.start();

            Common.logger.log(Level.INFO, "Server started on serverPort =<{0}>", serverPort);
        }
    }

    //-----------------------------------------------------------
    private void actionPerformedMenuItemServerStop(ActionEvent evt) {
        serverThread.interrupt();

        try {
            serverThread.join();
            Common.logger.info("server stopped");
        } catch (InterruptedException ex) {
            Logger.getLogger(TriggerIOTool.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //-----------------------------------------------------------
    private void initMenuServer() {
        menuServer = new JMenu("Server");

        menuServer.addMenuListener(new MenuListener() {

            public void menuSelected(MenuEvent e) {
                menuSelectedServer();
            }

            public void menuDeselected(MenuEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            public void menuCanceled(MenuEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }
        });

        menuItemServerStart = new JMenuItem("Start");
        menuItemServerStop = new JMenuItem("Stop");

        menuItemServerStart.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                actionPerformedMenuItemServerStart(e);
            }
        });

        menuItemServerStop.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                actionPerformedMenuItemServerStop(e);
            }
        });

        menuServer.add(menuItemServerStart);
        menuServer.add(menuItemServerStop);

        setServerMenuStatus();
    }

    //-----------------------------------------------------------
    private void initMenuFile() {
        menuFile = new JMenu("File");

        menuItemFileOpen = new JMenuItem("Open");
        menuItemFileSave = new JMenuItem("Save");
        menuItemFileSaveAs = new JMenuItem("Save As...");
        menuItemFileExit = new JMenuItem("Exit");

        menuItemFileOpen.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                actionPerformedMenuItemFileOpen(evt);
            }
        });
        menuItemFileSave.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                actionPerformedMenuItemFileSave(evt);
            }
        });

        menuItemFileSaveAs.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                actionPerformedMenuItemFileSaveAs(evt);
            }
        });

        menuItemFileExit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                ationPerformedMenuItemFileExit(evt);
            }
        });

        menuFile.add(menuItemFileOpen);
        menuFile.add(menuItemFileSave);
        menuFile.add(menuItemFileSaveAs);
        menuFile.add(menuItemFileExit);
    }

    //-----------------------------------------------------------
    private void initMenuDevice() {
        menuDevice = new JMenu("Device");

        menuDevice.addMenuListener(new MenuListener() {

            public void menuSelected(MenuEvent evt) {
                menuSelectedDevice(evt);
            }

            public void menuDeselected(MenuEvent evt) {
                //
            }

            public void menuCanceled(MenuEvent evt) {
                //
            }
        });
    }

    //-----------------------------------------------------------
    public static String getDeviceInfoHash(MidiDevice.Info deviceInfo) {
        return String.valueOf(
                deviceInfo.toString()
                + "|" + deviceInfo.getName()
                + "|" + deviceInfo.getDescription()
                + "|" + deviceInfo.getVendor()
                + "|" + deviceInfo.getVersion());
    }

    //-----------------------------------------------------------
    private void refreshReceivingDevices() {
        receivingDeviceInfos = Common.getReceivingDevices();
    }

    //-----------------------------------------------------------
    private void refreshTransmittingDevices() {
        transmittingDeviceInfos = Common.getTransmittingDevices();
    }

    //-----------------------------------------------------------
    private void menuSelectedDevice(MenuEvent evt) {
        String currentDeviceHashValue = "";

        if (triggerIODevice.isOpen()) {
            try {
                currentDeviceHashValue = getDeviceInfoHash(triggerIODevice.getInfo());
            } catch (Exception e) {
                Common.logger.info(e.getLocalizedMessage());
            }
            Common.logger.log(Level.FINEST, "currentDevice<{0}>", currentDeviceHashValue);
        }

        refreshReceivingDevices();
        menuDevice.removeAll();

        ButtonGroup buttonGroupMenuItemsDevice = new ButtonGroup();

        JRadioButtonMenuItem radioButtonMenuItemDeviceOffline = new JRadioButtonMenuItem("Offline");
        radioButtonMenuItemDeviceOffline.setActionCommand(OFFLINE);
        radioButtonMenuItemDeviceOffline.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                actionPerformedMenuItemDevice(evt);
            }
        });

        buttonGroupMenuItemsDevice.add(radioButtonMenuItemDeviceOffline);

        boolean selectedDevice = false;
        for (MidiDevice.Info deviceInfo : receivingDeviceInfos) {
            String foundDeviceHashValue = getDeviceInfoHash(deviceInfo);
            Common.logger.log(Level.FINEST, "found device<{0}>", foundDeviceHashValue);

            JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(deviceInfo.toString());
            menuItem.setActionCommand(foundDeviceHashValue);
            menuItem.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    actionPerformedMenuItemDevice(evt);
                }
            });

            if (foundDeviceHashValue.equals(currentDeviceHashValue)) {
                Common.logger.finest("devices match");
                selectedDevice = true;
                menuItem.setSelected(true);
            }

            buttonGroupMenuItemsDevice.add(menuItem);
            menuDevice.add(menuItem);
        }
        menuDevice.add(radioButtonMenuItemDeviceOffline);
        if (!selectedDevice) {
            radioButtonMenuItemDeviceOffline.setSelected(true);
        }
    }

    //-----------------------------------------------------------
    private void actionPerformedMenuItemDevice(ActionEvent evt) {
        if (evt.getActionCommand().equalsIgnoreCase(OFFLINE)) {
            goOffline();
        } else {
            for (MidiDevice.Info deviceInfo : receivingDeviceInfos) {
                if (getDeviceInfoHash(deviceInfo).equals(evt.getActionCommand())) {
                    goOnline(deviceInfo);
                }
            }
        }
    }

    //-----------------------------------------------------------
    private void actionPerformedMenuItemDownload(ActionEvent evt) {
        for (MidiDevice.Info deviceInfo : transmittingDeviceInfos) {
            if (getDeviceInfoHash(deviceInfo).equals(evt.getActionCommand())) {
                try {
                    download(deviceInfo);
                } catch (MidiUnavailableException ex) {
                    Common.logger.severe(TriggerIOTool.class.getName());
                }
            }
        }
    }

    private int showTransferOptionsDialog(String messageText, int imageType) {
        transferOptionsDialog.setMessageText(messageText);
        transferOptionsDialog.setImageIcon(imageType);
        transferOptionsDialog.setLocationRelativeTo(this);
        transferOptionsDialog.setVisible(true);

        return transferOptionsDialog.getReturnStatus();
    }

    //-----------------------------------------------------------
    private void download(MidiDevice.Info deviceInfo) throws MidiUnavailableException {
        Common.logger.info("Download");

        if (checkUnsavedChanges()) {
            DownloadReceiver receiver = new DownloadReceiver();
            MidiDevice midiDevice = MidiSystem.getMidiDevice(deviceInfo);
            midiDevice.open();

            Transmitter transmitter = midiDevice.getTransmitter();
            transmitter.setReceiver(receiver);

            int confirmDialog = showTransferOptionsDialog(
                    "<html>"
                    + "1. Request a Data Dump from the Trigger IO" + "<br>"
                    + "2. Press [OK] to complete the transfer"
                    + "</html>", TransferOptionsDialog.IMAGETYPE_DOWNLOAD);

            midiDevice.close();
            receiver.close();

            Common.logger.log(Level.FINE, "confirmDialog <{0}>", confirmDialog);

            switch (confirmDialog) {
                case TransferOptionsDialog.RET_OK:

                    if (!receiver.midiMessages.isEmpty()) {
                        triggerIODevice.kits = receiver.kits;
                        triggerIODevice.globalInputs = receiver.triggerInputs;

                        loadTables();
                        saveAsFile();

                    } else {
                        JOptionPane.showMessageDialog(this, "No sysex messages received", "Download", JOptionPane.ERROR_MESSAGE);

                    }
                    break;

                case TransferOptionsDialog.RET_CANCEL:
                    break;
            }
        } else {
            Common.logger.info("else??");
        }
    }

    //-----------------------------------------------------------
    private boolean checkUnsavedChanges() {
        boolean continueAction = false;
        if (fileChanged) {
            int confirmDialog = JOptionPane.showConfirmDialog(this, "Save changes to current file?", "Unsaved Changes", JOptionPane.YES_NO_CANCEL_OPTION);

            switch (confirmDialog) {
                case JOptionPane.YES_OPTION:
                    saveFile();
                    break;

                case JOptionPane.NO_OPTION:
                    continueAction = true;
                    break;

                case JOptionPane.CANCEL_OPTION:
                    break;
            }
        } else {
            continueAction = true;
        }
        return continueAction;
    }

    //-----------------------------------------------------------
    private void actionPerformedMenuItemFileOpen(ActionEvent evt) {
        if (checkUnsavedChanges()) {
            chooseFile();
        }
    }

    //-----------------------------------------------------------
    private void chooseFile() {
        JFileChooser fc = new JFileChooser(currentDirectory);
        fc.addChoosableFileFilter(fileNameExtensionFilter);

        int returnVal = fc.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {

            currentFile = fc.getSelectedFile();
            openFile();
        }
    }

    //-----------------------------------------------------------
    private void fileStatus(boolean isOpen) {
        fileChanged = false;
        menuItemFileSave.setEnabled(isOpen);
        menuItemFileSaveAs.setEnabled(isOpen);

        setEnabledTables(isOpen);

        if (isOpen) {
            currentDirectory = currentFile.getPath();

            labelFileName.setText(currentFile.toString());
            labelFileName.setForeground(Color.BLUE);
        } else {
            currentFile = null;

            labelFileName.setText("---");
            labelFileName.setForeground(Color.RED);
        }
    }

    //-----------------------------------------------------------
    private void openFile() {
        String ext = fileExtension();
        Common.logger.log(Level.INFO, "File extension <{0}>", ext);

        try {
            if (ext.equalsIgnoreCase(bundleVersion.getString("File.xml"))) {
                triggerIODevice.importXml(currentFile);

            } else if (ext.equalsIgnoreCase(bundleVersion.getString("File.sysex"))) {
                triggerIODevice.importSysex(currentFile);

            } else {
                Common.logger.log(Level.INFO, "File type not recognised");
                throw new Exception();
            }

            loadTables();
            fileStatus(true);
            syncDeviceToFile();

            Common.logger.log(Level.INFO, "File opened <{0}>", currentFile.toString());

        } catch (Exception ex) {
            Common.logger.log(Level.SEVERE, "Exception <{0}>", ex.getMessage());
            JOptionPane.showMessageDialog(this, "Invalid File", "Open File", JOptionPane.ERROR_MESSAGE);
            fileStatus(false);
            chooseFile();
        }
    }

    //-----------------------------------------------------------
    private void actionPerformedMenuItemFileSave(ActionEvent evt) {
        saveFile();
    }

    //-----------------------------------------------------------
    private void actionPerformedMenuItemFileSaveAs(ActionEvent evt) {
        saveAsFile();
    }

    //-----------------------------------------------------------
    private boolean setCurrentFile() {
        File selectedFile;

        boolean fileSet = false;

        JFileChooser fc = new JFileChooser(currentDirectory);
        fc.addChoosableFileFilter(fileNameExtensionFilter);

        int returnVal = fc.showSaveDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            selectedFile = fc.getSelectedFile();

            if (selectedFile.toString().endsWith("." + DEFAULTEXTENSION)) {
                currentFile = selectedFile;
            } else {
                currentFile = new File(selectedFile.toString() + "." + DEFAULTEXTENSION);
            }
            fileSet = true;
        }

        return fileSet;
    }

    //-----------------------------------------------------------
    private void saveAsFile() {
        if (setCurrentFile()) {
            saveCurrentFile();
        }
    }

    //-----------------------------------------------------------
    private void saveFile() {
        Common.logger.fine("begin");
        try {
            if (currentFile.exists()) {
                saveCurrentFile();
            } else {
                saveAsFile();
            }
        } catch (java.lang.NullPointerException e) {
            saveAsFile();
        }
        Common.logger.fine("end");
    }

    //-----------------------------------------------------------
    private String fileExtension() {
        return currentFile.getPath().substring(currentFile.getPath().lastIndexOf(".") + 1, currentFile.getPath().length());
    }

    //-----------------------------------------------------------
    private void saveCurrentFile() {
        String ext = fileExtension();
        Common.logger.log(Level.INFO, "File extension <{0}>", ext);

        currentDirectory = currentFile.getPath();

        try {

            if (ext.equalsIgnoreCase(bundleVersion.getString("File.xml"))) {
                triggerIODevice.exportXml(currentFile);

            } else if (ext.equalsIgnoreCase(bundleVersion.getString("File.sysex"))) {
                triggerIODevice.exportSyx(currentFile);

            } else {
                Common.logger.log(Level.INFO, "File type not recognised");
                throw new Exception();
            }

            fileStatus(true);

            Common.logger.log(Level.INFO, "Saved {0}", currentFile.getName());
            JOptionPane.showMessageDialog(this, "File " + currentFile.toString() + "\n Saved", "Save", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            Common.logger.severe(ex.getLocalizedMessage());
            JOptionPane.showMessageDialog(this, ex.getLocalizedMessage(), "Save", JOptionPane.ERROR_MESSAGE);

            saveAsFile();
        }
    }

    //-----------------------------------------------------------
    private void exitApp() {
        if (fileChanged) {
            int confirmDialog = JOptionPane.showConfirmDialog(this, "Save changes?", "Unsaved Changes", JOptionPane.YES_NO_CANCEL_OPTION);

            switch (confirmDialog) {
                case JOptionPane.YES_OPTION:
                    saveCurrentFile();
                    if (!fileChanged) {
                        this.dispose();
                    }
                    break;

                case JOptionPane.NO_OPTION:
                    this.dispose();
                    break;

                case JOptionPane.CANCEL_OPTION:
                    break;
            }
        } else {
            this.dispose();
        }
    }

    //-----------------------------------------------------------
    private void ationPerformedMenuItemFileExit(ActionEvent evt) {
        exitApp();
    }

    //-----------------------------------------------------------
    public void loadTables() {
        tableModelProgramChange.load(triggerIODevice.kits);
        tableModelMidiChannel.load(triggerIODevice.kits);
        tableModelMidiNote.load(triggerIODevice.kits);
        tableModelTriggerInput.load(triggerIODevice.globalInputs);

        for (GlobalInputMidi triggerInput : triggerIODevice.globalInputs) {
            tableMidiChannel.setValueAt(triggerInput.getTriggerInputName(), triggerInput.getTriggerInputNumber(), 0);
            tableMidiNote.setValueAt(triggerInput.getTriggerInputName(), triggerInput.getTriggerInputNumber(), 0);
        }

        this.repaint();

        Common.logger.fine("end");
    }

    //-----------------------------------------------------------
    private void tableChangedProgramChange(TableModelEvent evt) {
        Common.logger.log(Level.FINEST, "evt.getColumn()=<{0}>, evt.getFirstRow()=<{1}>", new Object[]{evt.getColumn(), evt.getFirstRow()});

        int kitNumber = evt.getColumn();
        int value = tableModelProgramChange.getRealValueAt(evt.getFirstRow(), evt.getColumn());

        triggerIODevice.kits.get(kitNumber).setProgramChange(value);
        updateDrumKit(kitNumber);
    }

    //-----------------------------------------------------------
    private void tableChangedMidiChannel(TableModelEvent evt) {
        Common.logger.log(Level.FINEST, "evt.getColumn()=<{0}>, evt.getFirstRow()=<{1}>", new Object[]{evt.getColumn(), evt.getFirstRow()});

        if (evt.getColumn() != 0) {
            int kitNumber = evt.getColumn() - 1;
            int inputNumber = evt.getFirstRow();
            int value = tableModelMidiChannel.getRealValueAt(evt.getFirstRow(), evt.getColumn());

            triggerIODevice.kits.get(kitNumber).inputs.get(inputNumber).setChannel(value);
            updateDrumKit(kitNumber);
        }
    }

    //-----------------------------------------------------------
    private void tableChangedMidiNote(TableModelEvent evt) {
        Common.logger.log(Level.FINEST, "evt.getColumn()=<{0}>, evt.getFirstRow()=<{1}>", new Object[]{evt.getColumn(), evt.getFirstRow()});

        if (evt.getColumn() != 0) {
            int kitNumber = evt.getColumn() - 1;
            int inputNumber = evt.getFirstRow();
            int value = tableModelMidiNote.getRealValueAt(evt.getFirstRow(), evt.getColumn());

            triggerIODevice.kits.get(kitNumber).inputs.get(inputNumber).setNote(value);
            updateDrumKit(kitNumber);
        }
    }

    //-----------------------------------------------------------
    private void tableChangedTriggerInput(TableModelEvent evt) {
        int inputNumber = evt.getFirstRow();

        triggerIODevice.globalInputs.set(inputNumber, tableModelTriggerInput.getTriggerInput(inputNumber));

        if (evt.getColumn() != 0) {
            updateTriggerInput(inputNumber);
        } else {
            tableMidiChannel.setValueAt(tableTriggerInput.getValueAt(inputNumber, 0), inputNumber, 0);
            tableMidiNote.setValueAt(tableTriggerInput.getValueAt(inputNumber, 0), inputNumber, 0);
        }
    }

    //-----------------------------------------------------------
    private void updateDrumKit(int kitNumber) {
        if (triggerIODevice.isOpen()) {
            try {
                triggerIODevice.sendtKit(kitNumber);
            } catch (MidiUnavailableException ex) {
                Common.logger.severe(ex.getLocalizedMessage());
            } catch (InvalidMidiDataException ex) {
                Common.logger.severe(ex.getLocalizedMessage());
            }
        }
        fileChanged();
    }

    //-----------------------------------------------------------
    private void updateTriggerInput(int inputNumber) {
        if (triggerIODevice.isOpen()) {
            try {
                triggerIODevice.sendTriggerInput(inputNumber);
            } catch (MidiUnavailableException ex) {
                Common.logger.severe(ex.getLocalizedMessage());
            } catch (InvalidMidiDataException ex) {
                Common.logger.severe(ex.getLocalizedMessage());
            }
        }
        fileChanged();
    }

    //-----------------------------------------------------------
    private void fileChanged() {
        fileChanged = true;
    }

    //-----------------------------------------------------------
    private void goOffline() {
        if (triggerIODevice.isOpen()) {
            triggerIODevice.close();
        }
        labelDeviceName.setText(OFFLINE);
        labelDeviceName.setForeground(Color.RED);

        Common.logger.fine("Offline");
    }

    //-----------------------------------------------------------
    private void goOnline(MidiDevice.Info deviceInfo) {
        try {
            triggerIODevice.open(deviceInfo);
            labelDeviceName.setText(deviceInfo.getName());
            labelDeviceName.setForeground(Color.BLUE);

            syncDeviceToFile();
        } catch (MidiUnavailableException ex) {
            Common.logger.log(Level.WARNING, "MidiUnavailableException, {0}{1}{2}", new Object[]{ex.getMessage(), ex.toString(), ex.getStackTrace()});
            JOptionPane.showMessageDialog(this, "This device is unavailable:\n" + deviceInfo.getDescription(), "Device Unavailable", JOptionPane.ERROR_MESSAGE);
            goOffline();
        }
    }

    //-----------------------------------------------------------
    private void syncDeviceToFile() throws MidiUnavailableException {
        if (triggerIODevice.isOpen()) {
            try {
                if (currentFile.exists()) {
                    Common.logger.fine("file exists");
                } else {
                    chooseFile();
                }

                int confirmDialog = showTransferOptionsDialog(
                        "<html>"
                        + "The device will be synchronised with the current loaded file." + "<br>"
                        + "This will overwrite ALL of the Trigger IO device settings." + "<br>"
                        + "Press [OK] to complete the transfer"
                        + "</html>", TransferOptionsDialog.IMAGETYPE_UPLOAD);

                switch (confirmDialog) {
                    case TransferOptionsDialog.RET_OK:
                        try {
                            triggerIODevice.send();
                        } catch (InvalidMidiDataException ex) {
                            Common.logger.severe(ex.toString());
                            JOptionPane.showMessageDialog(this, ex.getMessage() + ex.toString(), "Something went wrong", JOptionPane.ERROR_MESSAGE);
                            goOffline();
                        }
                        break;
                    case TransferOptionsDialog.RET_CANCEL:
                        goOffline();
                        break;
                }

            } catch (NullPointerException ex) {
                Common.logger.warning("problem with file");
                chooseFile();
            }
        }
    }

    //-----------------------------------------------------------
    private void keyListenerTableMidiNote(KeyEvent evt) {
        Common.logger.finer(String.valueOf(evt.getKeyCode()));
        Common.logger.log(Level.FINER, "{0}, {1}", new Object[]{tableMidiNote.getEditingRow(), tableMidiNote.getEditingColumn()});
        int editingRow = tableMidiNote.getEditingRow();
        int editingColumn = tableMidiNote.getEditingColumn();

        if (editingRow != -1 && editingColumn != -1) {
            Common.logger.finest("currentRealValue");
            int currentRealValue = tableModelMidiNote.getRealValueAt(editingRow, editingColumn);
            Object newValue = null;

            switch (evt.getKeyCode()) {
                case 44:
                    newValue = lovTriggerMidiNote.getItemAt(currentRealValue - 1);
                    Common.logger.log(Level.FINEST, "< pressed, new value =<{0}>", newValue.toString());
                    break;
                case 46:
                    newValue = lovTriggerMidiNote.getItemAt(currentRealValue + 1);
                    Common.logger.log(Level.FINEST, "> pressed, new value =<{0}>", newValue.toString());
                    break;
            }
            if (newValue != null) {
                Common.logger.finest("set value at");
                tableModelMidiNote.setValueAt(newValue, editingRow, editingColumn);
            }
        }
    }

    //-----------------------------------------------------------
    private void addWindowListeners() {
        this.addWindowListener(new WindowListener() {

            public void windowClosing(WindowEvent e) {
                exitApp();
            }

            public void windowOpened(WindowEvent e) {
            }

            public void windowClosed(WindowEvent e) {
            }

            public void windowIconified(WindowEvent e) {
            }

            public void windowDeiconified(WindowEvent e) {
            }

            public void windowActivated(WindowEvent e) {
            }

            public void windowDeactivated(WindowEvent e) {
            }
        });
    }
    //-----------------------------------------------------------
    JPanel panelMain;
    JPanel panelButtons;
    JTabbedPane tabbedPaneMain;
    JPanel panelProgramChange;
    JPanel panelMidiChannel;
    JPanel panelMidiNote;
    JPanel panelTriggerInput;
    JLabel labelFileName;
    JLabel labelDeviceName;
    JMenuBar menuBarMain;
    JMenu menuServer;
    JMenu menuFile;
    JMenu menuDevice;
    JMenu menuHelp;
    JMenu menuDownload;
    JMenuItem menuItemFileOpen;
    JMenuItem menuItemFileSave;
    JMenuItem menuItemFileSaveAs;
    JMenuItem menuItemFileExit;
    JMenuItem menuItemHelpAbout;
    JMenuItem menuItemServerStart;
    JMenuItem menuItemServerStop;
    JTable tableProgramChange;
    JTable tableMidiChannel;
    JTable tableMidiNote;
    JTable tableTriggerInput;
    TableModelProgramChange tableModelProgramChange;
    TableModelMidiChannel tableModelMidiChannel;
    TableModelMidiNote tableModelMidiNote;
    TableModelTriggerInput tableModelTriggerInput;
    JComboBox lovGain;
    JComboBox lovProgramChange;
    JComboBox lovRetrigger;
    JComboBox lovThreshold;
    JComboBox lovTriggerMidiChannel;
    JComboBox lovTriggerMidiNote;
    JComboBox lovTriggerType;
    JComboBox lovVelocityCurve;
    JComboBox lovXTalk;
    boolean fileChanged;
    File currentFile;
    List<MidiDevice.Info> receivingDeviceInfos;
    List<MidiDevice.Info> transmittingDeviceInfos;
    DeviceMidi triggerIODevice;
    final static String OFFLINE = "OFFLINE";
    final ResourceBundle bundleVersion = ResourceBundle.getBundle("triggeriotool");
    final FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter(bundleVersion.getString("File.description"), bundleVersion.getString("File.xml"), bundleVersion.getString("File.sysex"));
    final String DEFAULTEXTENSION = bundleVersion.getString("File.xml");
    String currentDirectory = System.getProperty("user.dir");
    TransferOptionsDialog transferOptionsDialog;
    int serverPort = 4444;
    Thread serverThread = new Thread();
}