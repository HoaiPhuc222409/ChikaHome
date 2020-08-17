package com.example.chikaapp.mqtt;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.chikaapp.action.CustomToast;

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

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressPie;

public class MQTTService implements MqttCallback {

    final static String URL = "tcp://soldier.cloudmqtt.com:16607";
    final static String UserName = "pcnlljoy";
    final static String Password = "q2zXZf4CSUUE";

    public static String stringTopic = "";

    public static String stringMessage = "";

    //Create MQTTClient
    String clientId = MqttClient.generateClientId();
    MqttAndroidClient client;
    ACProgressPie loadingDialog;

    private static MQTTService mqttService;
    private Context context;

    listener listener;

    public MQTTService(Context context, listener listener) {
        this.context = context;
        this.listener = listener;

    }

    public void connect() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(UserName);
        options.setPassword(Password.toCharArray());
        options.setCleanSession(true);
        options.setKeepAliveInterval(1000);
        try {
            client = new MqttAndroidClient(context, URL
                    , clientId, new MemoryPersistence());
            client.setCallback(this);
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    listener.onConnected(true);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken
                        , Throwable exception) {
                    listener.onConnected(false);
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    public void publish(String topic, String payload, String type) {
        try {
            if (client.isConnected() == false) {
                client.connect();
            }

            MqttMessage message = new MqttMessage();
            message.setPayload(payload.getBytes());
            message.setQos(2);
            if (type.equals("SW2") || type.equals("SW3")) {
                message.setRetained(true);
            } else {
                message.setRetained(false);
            }

            client.publish(topic, message, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i("fuck", "topic " + topic + " mess:" + message);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i("fuck", "publish failed!");
                }
            });
        } catch (MqttException e) {
            Log.e("hello", e.toString());
            e.printStackTrace();
        }
    }

    public static MQTTService getInstance(Context context, listener listener) {
        if (mqttService == null)
            mqttService = new MQTTService(context, listener);
        return mqttService;
    }

    public void subscribe(String topic, int qos) {

        try {
            if (client.isConnected()) {
                client.subscribe(topic, qos, null
                        , new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        //subscribe topic success
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken
                            , Throwable exception) {
                        //subscribe topic fail
                    }
                });
            } else {
                reconnect();
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    public void disconnect() {
        try {
            IMqttToken disToken = client.disconnect();
            disToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // we are now successfully disconnected
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    // something went wrong, but probably we are disconnected anyway
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void unSubscribe(final String topic) {
        try {
            IMqttToken subToken = client.unsubscribe(topic);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                   //unsubscribe success
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken
                        , Throwable exception) {
                    //unsubscribe fail
                }
            });

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void reconnect() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(UserName);
        options.setPassword(Password.toCharArray());
        options.setCleanSession(true);
        options.setKeepAliveInterval(1000);
        while (!client.isConnected()) {
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
        stringTopic = topic;
        stringMessage = message.toString();
        listener.onReceive(stringTopic, stringMessage);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }

    public interface listener {
        void onReceive(String topic, String mess);

        void onConnected(boolean state);
    }
}
