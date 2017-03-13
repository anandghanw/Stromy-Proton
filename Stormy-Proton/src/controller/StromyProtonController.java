package controller;

import arduino.ArduinoReceiver;
import file.FileUtils;
import ui.Window;

import java.awt.*;

/**
 * Created by Anandghan W on 3/13/17.
 */
public class StromyProtonController {
    public static void main(String[] args){
        ArduinoReceiver arduinoReceiver = new ArduinoReceiver(10000,100);
        Window window = new Window(700,500);
        window.setBackgroundColor(Color.DARK_GRAY);
        while(!arduinoReceiver.arduinoConnect.isArduinoConnected); //Wait for the arduino to connect
        for(int i=0; i<1000; i++){
            int data  = (int) arduinoReceiver.ringBuffer.get();
            window.setText("Value = " + data);
            FileUtils.writeToFile( System.getProperty("user.dir")+"/","data.txt",Integer.toString(data));
        }
    }
}
