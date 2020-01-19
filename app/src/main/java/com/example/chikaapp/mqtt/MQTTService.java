package com.example.chikaapp.mqtt;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQTTService implements MqttCallback {

    final static String URL = "tcp://14.186.18.140:2502";

    public static String stringTopic="";

    public static String stringMessage="";

    //Create MQTTClient
    String clientId = MqttClient.generateClientId();
    MqttAndroidClient client;

    private static MQTTService mqttService;
    private Context context;

    listener listener;

    public MQTTService(Context context, listener listener) {
        this.context = context;
        this.listener=listener;

        Log.i("context", context.toString());

        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName("chika");
        options.setPassword("2502".toCharArray());
        options.setCleanSession(true);
        options.setKeepAliveInterval(1000);

        try {
            client = new MqttAndroidClient(context, URL, clientId, new MemoryPersistence());
            client.setCallback(this);
            IMqttToken token=client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    subscribe("light",1);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(context, "Not connect", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }


    }

    public void publish(String topic, String message) {
        MqttMessage mqttMessage = new MqttMessage(message.getBytes());
        try {
            client.publish(topic, mqttMessage);
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    public static MQTTService getInstance(Context context,listener listener) {
        if (mqttService == null)
            mqttService = new MQTTService(context,listener);
        return mqttService;
    }

    public void subscribe(String topic,int qos){
        try {
            client.subscribe(topic,qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void reconnect(){
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName("chika");
        options.setPassword("2502".toCharArray());
        options.setCleanSession(true);
        options.setKeepAliveInterval(1000);
        while (!client.isConnected()){
            try {
                client.connect(options);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public void connectionLost(Throwable cause) {
        Toast.makeText(context, "connection LOST", Toast.LENGTH_SHORT).show();
        reconnect();
    }


    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
//        Toast.makeText(context, "topic: " + topic + "\tMess: " + message.toString(), Toast.LENGTH_SHORT).show();
        stringTopic=topic;
        stringMessage=message.toString();
        listener.onReceive(message.toString());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }
    public interface listener{
        void onReceive(String mess);
    }

}
