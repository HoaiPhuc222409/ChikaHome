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

    final static String URL = "tcp://chika.gq:2502";

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
                    subscribe("6e2b871e-fd51-4006-af7b-a3ab59b17c40/6883dd85-c759-428c-98fe-77b00c20c710",2);
                    subscribe("6e2b871e-fd51-4006-af7b-a3ab59b17c40/7f669cb3-2189-4c22-ae09-cb6cc663b96d",2);
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
        mqttMessage.setRetained(true);
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
        stringTopic=topic;
        stringMessage=message.toString();
        listener.onReceive1(stringTopic, stringMessage);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }
    public interface listener{
        void onReceive1(String topic, String mess);
    }

}
