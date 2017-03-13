package arduino;

import org.zu.ardulink.Link;
import org.zu.ardulink.RawDataListener;
import org.zu.ardulink.gui.ConnectionStatus;
import org.zu.ardulink.gui.SerialConnectionPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Anandghan W on 3/7/17.
 */
public class ArduinoConnect {

    public boolean isArduinoConnected = false;
    protected  String mostRecentMessage;
    protected  boolean isDataSampled = false;
    protected ArduinoReceiver arduinoReceiver;

    protected ArduinoConnect(boolean isDataSampled,ArduinoReceiver arduinoReceiver){
        this.isDataSampled = isDataSampled;
        this.arduinoReceiver = arduinoReceiver;
    }

    protected void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // set the application
                    ArduinoConnect.SerialRead application = new ArduinoConnect.SerialRead();
                    // show the frame
                    application.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    protected  class SerialRead implements RawDataListener {

        public JFrame frame;
        private SerialConnectionPanel serialCOM;
        private JButton connect;
        private JButton disconnect;

        private final Link link = Link.getDefaultInstance();

        public SerialRead() {
            this.frame = new JFrame();
            initGUI();
            runApp();
        }

        private void initGUI() {

            // init the frame
            frame.setTitle("Arduino Serial Read");
            frame.setBounds(0, 0, 700, 100);
            frame.setMinimumSize(new Dimension(100, 100));
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // init control panel
            JPanel ctrlPanel = new JPanel();
            ctrlPanel.setLayout(new FlowLayout());

            // init the serial connection panel
            serialCOM = new SerialConnectionPanel();
            ctrlPanel.add(serialCOM);
            frame.getContentPane().add(ctrlPanel, BorderLayout.SOUTH);
            serialCOM.setBaudRateVisible(false);

            // init the connection status
            ConnectionStatus connectionStatus = new ConnectionStatus();
            ctrlPanel.add(connectionStatus);

            // init connect and disconnect button
            connect = new JButton("connect");
            ctrlPanel.add(connect);
            disconnect = new JButton("disconnect");
            ctrlPanel.add(disconnect);


        }

        private void runApp() {

            // Register a RawDataListener to receive data from Arduino
            link.addRawDataListener(this);

            // add an action listener to the connect button
            connect.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    String comPort = serialCOM.getConnectionPort();
                    String baudRateS = serialCOM.getBaudRate();
                    if (comPort == null || "".equals(comPort)) {
                        JOptionPane.showMessageDialog(connect, "Invalid COM PORT.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else if (baudRateS == null || "".equals(baudRateS)) {
                        JOptionPane.showMessageDialog(connect, "Invalid baud rate. Advice: set " + Link.DEFAULT_BAUDRATE, "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        try {
                            int baudRate = Integer.parseInt(baudRateS);
                            // init the connection between Arduino and PC
                            boolean connected = link.connect(comPort, baudRate);
                            if (connected) {
                                connect.setEnabled(false);
                                serialCOM.setEnabled(false);
                                disconnect.setEnabled(true);
                                isArduinoConnected = true;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            String message = ex.getMessage();
                            if (message == null || message.trim().equals("")) {
                                message = "Generic Error on connection";
                            }
                            JOptionPane.showMessageDialog(connect, message, "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });

            // add an  action  listener to disconnect button
            disconnect.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    boolean disconnected = link.disconnect();
                    if (disconnected) {
                        connect.setEnabled(true);
                        serialCOM.setEnabled(true);
                        disconnect.setEnabled(false);
                        isArduinoConnected = false;
                    }
                }
            });

        }

        @Override
        public void parseInput(String id, int numBytes, int[] message) {
            try{
                StringBuilder build = new StringBuilder(numBytes + 1);
                for (int i = 0; i < numBytes; i++) {
                    build.append((char) message[i]);
                }
                String msgString = build.toString();
                if(msgString.equals("Error")) {
                    System.out.println("Error fetching data from Arduino");
                    return;
                }
                mostRecentMessage = msgString;
                if(isDataSampled == false){
                    arduinoReceiver.processData(msgString);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
