package com.example.chikaapp.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chikaapp.R;
import com.example.chikaapp.model.Devices;

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
import org.json.JSONObject;

import java.util.ArrayList;

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.ViewHolder>  {

    final static String URL = "tcp://chika.gq:2502";

    static String TAG="debug";
    String clientId = MqttClient.generateClientId();
    private ArrayList<Devices> devicesArrayList;
    public int imgId;
    Context context;
    MqttAndroidClient mqttAndroidClient;

    public DevicesAdapter(ArrayList<Devices> devicesArrayList, Context context) {
        this.devicesArrayList = devicesArrayList;
        this.context = context;
    }

    public void updateList(ArrayList<Devices> devicesArrayList) {
        this.devicesArrayList = devicesArrayList;
    }

    @NonNull
    @Override
    public DevicesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_device, parent, false);
        DevicesAdapter.ViewHolder viewHolder = new DevicesAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DevicesAdapter.ViewHolder holder, int position) {
        Devices devices=devicesArrayList.get(position);

        imgId=getDevicesImage(devices.getLogo());

        holder.tv_devices_name.setText(devices.getName());
        holder.img_device.setImageResource(imgId);

        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName("chika");
        options.setPassword("2502".toCharArray());
        options.setCleanSession(true);
        options.setKeepAliveInterval(1000);

        mqttAndroidClient = new MqttAndroidClient(context, URL, clientId, new MemoryPersistence());
        try {
            mqttAndroidClient.connect(options, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    subscribe(devicesArrayList.get(position).getTopic());
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        mqttAndroidClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Log.i(TAG, "connection lost");
                reconnect();
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String type, stringMessage;
                stringMessage = message.toString();
                if (topic.equals(devicesArrayList.get(position).getTopic())){
                   type=devicesArrayList.get(position).getType();
                   if (type.equals("SW2")||type.equals("SW3")||type.equals("SR2")||type.equals("SR3")){
                        if (stringMessage.equals("1")){
                            holder.tv_Status.setText("ON");
                            holder.tv_Status.setTextColor(context.getResources().getColor(R.color.green));
                            devicesArrayList.get(position).setState(true);
                        } else {
                            holder.tv_Status.setText("OFF");
                            holder.tv_Status.setTextColor(context.getResources().getColor(R.color.red));
                            devicesArrayList.get(position).setState(false);
                        }
                   } else if (type.equals("SS01")){
                        JSONObject jsonObject=new JSONObject(stringMessage);
                        boolean alert = jsonObject.getBoolean("alert");
                        boolean state = jsonObject.getBoolean("state");
                        if (state){
                            holder.img_device.setImageResource(R.drawable.door_open_icon);
                            holder.tv_Status.setText("OPENED");
                            holder.tv_Status.setTextColor(context.getResources().getColor(R.color.blue));
                            devicesArrayList.get(position).setState(true);
                        } else {
                            holder.img_device.setImageResource(R.drawable.door_close_icon);
                            holder.tv_Status.setText("CLOSED");
                            holder.tv_Status.setTextColor(context.getResources().getColor(R.color.red));
                            devicesArrayList.get(position).setState(false);
                        }


                   }else if (type.equals("SS03")){
                       holder.img_device.setVisibility(View.GONE);
                       holder.tv_Status.setVisibility(View.GONE);
                       holder.tv_air_quality.setVisibility(View.VISIBLE);
                       holder.img_status_air_quality.setVisibility(View.VISIBLE);
                       JSONObject jsonObject = new JSONObject(stringMessage);
                       float temperature = (float)jsonObject.getDouble("temperature");
                       float humidity = (float) jsonObject.getLong("humidity");
                       float aqi = (float)jsonObject.getDouble("aqi");

                       holder.tv_humidity.setText(""+humidity);
                       holder.tv_temperature.setText(""+temperature);

                       if (aqi>0&&aqi<=2){
                            holder.tv_air_quality.setText(R.string.air_safe);
                            holder.img_status_air_quality.setImageResource(R.drawable.img_safe1);
                            holder.tv_air_quality.setTextColor(context.getResources().getColor(R.color.green));
                       } else if (aqi>2&&aqi<6.5){
                           holder.tv_air_quality.setText(R.string.air_instability);
                           holder.img_status_air_quality.setImageResource(R.drawable.img_warning);
                           holder.tv_air_quality.setTextColor(context.getResources().getColor(R.color.orange));
                       } else {
                           holder.tv_air_quality.setText(R.string.air_dangerous);
                           holder.img_status_air_quality.setImageResource(R.drawable.img_dangerous);
                           holder.tv_air_quality.setTextColor(context.getResources().getColor(R.color.red));
                       }

                   }

                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.i(TAG, "msg delivered");
            }
        });
    }

    @Override
    public int getItemCount() {
        return devicesArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_device, img_status_air_quality;
        TextView tv_devices_name, tv_Status, tv_air_quality, tv_humidity, tv_temperature;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_device = itemView.findViewById(R.id.img_devices);
            tv_devices_name = itemView.findViewById(R.id.tv_devices_name);
            tv_Status = itemView.findViewById(R.id.tvStatus);
            tv_air_quality = itemView.findViewById(R.id.tvAirQuality);
            img_status_air_quality = itemView.findViewById(R.id.img_air_quality);
            tv_humidity = itemView.findViewById(R.id.humidity);
            tv_temperature=itemView.findViewById(R.id.temperature);
        }
    }

    public int getDevicesImage(String roomImageName) {
        switch (roomImageName) {
            case "light": {
                imgId = R.drawable.light_icon;
                break;
            }
            case "chandelier":{
                imgId = R.drawable.chandelier_icon;
                break;
            }
            case "fan": {
                imgId = R.drawable.ac_fan_icon;
                break;
            }
            case "air":{
                imgId = R.drawable.ca_ss03;
                break;
            }
            case "television": {
                imgId = R.drawable.television_icon;
                break;
            }
            case "curtain": {
                imgId = R.drawable.curtain_icon;
                break;
            }
            case "door":
                imgId = R.drawable.door_close_icon;
                break;
            case "speaker":
                imgId = R.drawable.speaker_icon;
                break;
            case "air-conditioner":
                imgId = R.drawable.air_conditioner_icon;
                break;
            case "lamp":
                imgId=R.drawable.lamp_icon;
                break;
            case "garden-light":
                imgId=R.drawable.garden_light_icon;
                break;
            case "selling-fan":
                imgId=R.drawable.selling_fan_icon;
                break;
            default: imgId = R.drawable.bg_button;
        }
        return imgId;
    }

    public void subscribe(String topic) {
        try {
            mqttAndroidClient.subscribe(topic, 2, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i("hello", "subscribed succeed");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i("hello", "subscribed failed");
                }
            });

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void reconnect() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName("chika");
        options.setPassword("2502".toCharArray());
        options.setCleanSession(true);
        options.setKeepAliveInterval(1000);
        while (!mqttAndroidClient.isConnected()) {
            try {
                mqttAndroidClient.connect(options);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }
}
