package com.example.chikaapp.fragment;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ButtonNotUsedFragment extends Fragment implements View.OnLongClickListener {

    final static String URL = "tcp://chika.gq:2502";
    String clientId;
    MqttAndroidClient mqttAndroidClient;
    Button button1, button2, button3;
    String idRoom, topic, type;
    int max;
    CommunicationInterface listener;


    public ButtonNotUsedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CommunicationInterface) {
            listener = (CommunicationInterface) context;
        } else {
            throw new RuntimeException(context.toString() + "Can phai implement");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //MQTTConnection
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName("chika");
        options.setPassword("2502".toCharArray());
        options.setCleanSession(true);
        options.setKeepAliveInterval(5000);

        //bundle
        Bundle bundle = getArguments();
        ArrayList arrayList = bundle.getIntegerArrayList("button");
        max = bundle.getInt("max");
        idRoom = bundle.getString("idRoom");
        topic = bundle.getString("topic");
        type = bundle.getString("type");

        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_button_not_used, container, false);
        initialize(view);
        if (max == 2) {
            button3.setVisibility(View.GONE);
        }
        for (int i = 0; i < arrayList.size(); i++) {
            setButtonVisible(arrayList.get(i).toString());
        }

        mqttAndroidClient = new MqttAndroidClient(getContext(), URL, clientId, new MemoryPersistence());
        try {
            mqttAndroidClient.connect(options, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    button1.setOnClickListener(view1 -> {
                        if (button1.getText().toString().equals("Button 1")
                                ||button1.getText().toString().equals("Nút 1")
                                ||button1.getText().toString().equals("OFF")){
                            publish(getTopicButton(type, topic, "1"),"1",type);
                            button1.setText("ON");
                        } else if (button1.getText().toString().equals("ON")){
                            publish(getTopicButton(type, topic, "1"),"0",type);
                            button1.setText("OFF");
                        }
                    });

                    button2.setOnClickListener(view1 -> {
                        if (button2.getText().toString().equals("Button 2")
                                ||button2.getText().toString().equals("Nút 2")
                                ||button2.getText().toString().equals("OFF")){
                            publish(getTopicButton(type, topic, "2"),"1",type);
                            button2.setText("ON");
                        } else if (button2.getText().toString().equals("ON")){
                            publish(getTopicButton(type, topic, "2"),"0",type);
                            button2.setText("OFF");
                        }
                    });

                    button3.setOnClickListener(view1 -> {
                        if (button3.getText().toString().equals("Button 3")
                                ||button3.getText().toString().equals("Nút 3")
                                ||button3.getText().toString().equals("OFF")){
                            publish(getTopicButton(type, topic, "3"),"1",type);
                            button3.setText("ON");
                        } else if (button3.getText().toString().equals("ON")){
                            publish(getTopicButton(type, topic, "3"),"0",type);
                            button3.setText("OFF");
                        }
                    });

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
        return view;
    }

    public void initialize(View view) {
        //MQTT
        clientId = MqttClient.generateClientId();

        //initView
        button1 = view.findViewById(R.id.btn_1);
        button2 = view.findViewById(R.id.btn_2);
        button3 = view.findViewById(R.id.btn_3);

        button1.setOnLongClickListener(this);
        button2.setOnLongClickListener(this);
        button3.setOnLongClickListener(this);
    }

    public void setButtonVisible(String a) {
        switch (a) {
            case "1.0":
                button1.setVisibility(View.GONE);
                break;
            case "2.0":
                button2.setVisibility(View.GONE);
                break;
            case "3.0":
                button3.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.btn_1:
                listener.ButtonNotUsedToAddDevice(1, idRoom, getTopicButton(type, topic, "1"), type);
                break;
            case R.id.btn_2:
                listener.ButtonNotUsedToAddDevice(2, idRoom, getTopicButton(type, topic, "2"), type);
                break;
            case R.id.btn_3:
                listener.ButtonNotUsedToAddDevice(3, idRoom, getTopicButton(type, topic, "3"), type);
                break;
        }
        return true;
    }

    public String getTopicButton(String type, String topicHead, String button) {
        String TopicFinal = "";
        if (type.equals("SW2") || type.equals("SW3")) {
            TopicFinal = topicHead + "/button" + button;
        } else if (type.equals("SR2")||type.equals("SR3")){
            TopicFinal = topicHead;
        }
        return TopicFinal;
    }

    public void publish(String topic, String payload, String type) {
        try {
            if (mqttAndroidClient.isConnected() == false) {
                mqttAndroidClient.connect();
            }

            MqttMessage message = new MqttMessage();
            message.setPayload(payload.getBytes());
            message.setQos(2);
            if (type.equals("SW2")||type.equals("SW3")||type.equals("SR2")||equals("SR3")){
                message.setRetained(true);
            } else {
                message.setRetained(false);
            }

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
