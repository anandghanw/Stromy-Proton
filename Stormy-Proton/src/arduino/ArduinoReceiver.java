package arduino;

import dataObjects.RingBuffer;
import dataObjects.RingBufferElement;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Anandghan W on 3/7/17.
 */
public class ArduinoReceiver {
    public RingBuffer ringBuffer;
    public int sampleRate;
    public ArduinoConnect arduinoConnect;
    private Timer collectDataTimer;
    private TimerTask collectDataTask;

    /*When sample rate needs to be enforced*/
    public ArduinoReceiver(int bufferLength, int sampleRate){
        this.sampleRate=sampleRate;
        arduinoConnect = new ArduinoConnect(true,this);
        arduinoReceiverCommon(bufferLength);

        collectDataTimer = new Timer();
        collectDataTask = new TimerTask() {
            @Override
            public void run() {
                processData();
            }
        };
        collectDataTimer.scheduleAtFixedRate(collectDataTask,0,1000/sampleRate);//delay,time in between tasks
    }

    /*No sample rate. Collect as and when available*/
    public ArduinoReceiver(int bufferLength){
        arduinoConnect = new ArduinoConnect(false,this);
        arduinoReceiverCommon(bufferLength);
    }

    /*Add your message parser logic here*/
    private int messageParser(String msg){
        return Integer.parseInt(msg);
    }

    private void arduinoReceiverCommon(int bufferLength){
        ringBuffer = new RingBuffer<RingBufferElement>(bufferLength);
        for(int i=0;i<ringBuffer.capacity();i++){
            ringBuffer.add(new RingBufferElement(0));
        }
        arduinoConnect.start();
    }

    protected synchronized void processData(String msg){
        int data = messageParser(msg);
        ringBuffer.add(new RingBufferElement(data));
    }

    private synchronized void processData(){
        if(arduinoConnect.isArduinoConnected)
            processData(arduinoConnect.mostRecentMessage);
    }

}

