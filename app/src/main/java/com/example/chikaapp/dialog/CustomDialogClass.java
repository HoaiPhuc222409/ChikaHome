package com.example.chikaapp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.chikaapp.R;
import com.example.chikaapp.action.RecyclerItemClickListener;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class CustomDialogClass extends Dialog {

    final static String URL = "tcp://chika.gq:2502";
    String clientId;
    MqttAndroidClient mqttAndroidClient;
    Context context;
    Button btnOk;
    public CustomDialogClass(@NonNull Context context) {
        super(context);
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_alert);
        //bundle

        //init
        clientId = MqttClient.generateClientId();
        btnOk = findViewById(R.id.btn_dialog);
        //MQTT
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName("chika");
        options.setPassword("2502".toCharArray());
        options.setCleanSession(true);
        options.setKeepAliveInterval(1000);

        mqttAndroidClient = new MqttAndroidClient(getContext(), URL, clientId, new MemoryPersistence());
        try {
            mqttAndroidClient.connect(options, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    btnOk.setOnClickListener(view -> {

                    });
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    public void publish(String topic, String payload) {
        try {
            if (mqttAndroidClient.isConnected() == false) {
                mqttAndroidClient.connect();
            }

            MqttMessage message = new MqttMessage();
            message.setPayload(payload.getBytes());
            message.setQos(2);
            message.setRetained(true);
            mqttAndroidClient.publish(topic, message,null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i ("debug", "publish succeed! ") ;
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i("debug", "publish failed!") ;
                }
            });
        } catch (MqttException e) {
            Log.e("hello", e.toString());
            e.printStackTrace();
        }
    }
}
