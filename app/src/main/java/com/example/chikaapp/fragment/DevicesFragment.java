package com.example.chikaapp.fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chikaapp.R;
import com.example.chikaapp.SharedPreferencesUtils;
import com.example.chikaapp.action.CustomToast;
import com.example.chikaapp.action.RecyclerItemClickListener;
import com.example.chikaapp.adapter.DevicesAdapter;
import com.example.chikaapp.api.ApiRetrofit;
import com.example.chikaapp.api.DeviceUtils;
import com.example.chikaapp.model.DeleteResponse;
import com.example.chikaapp.model.Devices;
import com.example.chikaapp.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.ArrayList;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressPie;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class DevicesFragment extends Fragment implements View.OnClickListener {

    final static String URL = "tcp://soldier.cloudmqtt.com:16607";
    final static String UserName = "pcnlljoy";
    final static String PassWord = "q2zXZf4CSUUE";
    String clientId;
    MqttAndroidClient mqttAndroidClient;

    String idRoom, state;
    Switch stateSwitch;
    DeviceUtils deviceUtils;
    TextView tvRoomName, tv_humidity, tv_temperature;
    ImageView imgRefresh, imgAddDevice;
    DevicesAdapter devicesAdapter;
    Context context;
    ArrayList<Devices> devicesArrayList;
    RecyclerView rclDevices;
    CommunicationInterface listener;
    SwipeRefreshLayout refreshLayout;

    ACProgressPie loadingDialog;

    public DevicesFragment() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_devices, container, false);

        initialize(view);

        //MQTTConnection
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(UserName);
        options.setPassword(PassWord.toCharArray());
        options.setCleanSession(true);
        options.setKeepAliveInterval(1000);

        //bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            idRoom = bundle.getString("idRoom");
            tvRoomName.setText(bundle.getString("roomName"));
            getDataDevices(SharedPreferencesUtils.loadToken(getContext()), idRoom);
        } else {
            Toast.makeText(getContext(), "Can't receive data!", Toast.LENGTH_SHORT).show();
        }

        mqttAndroidClient = new MqttAndroidClient(getContext(), URL, clientId, new MemoryPersistence());
        try {
            mqttAndroidClient.connect(options, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    rclDevices.addOnItemTouchListener(
                            new RecyclerItemClickListener(context, rclDevices, new RecyclerItemClickListener.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    String type = devicesArrayList.get(position).getType();
                                    int swBtn = devicesArrayList.get(position).getSwitchButton();
                                    boolean state = devicesArrayList.get(position).isState();
                                    String topic = devicesArrayList.get(position).getTopic();

                                    if (type.equals("SW2") || type.equals("SW3")) {
                                        if (state) {
                                            publish(topic, "false", type);
                                        } else {
                                            publish(topic, "true", type);
                                        }
                                    } else if (type.equals("SR2") || type.equals("SR3")) {
                                        String RFCommunication;
                                        if (state) {
                                            RFCommunication ="{\"type\": " + "\"SR\"" + ",\"button\": " + swBtn + ",\"state\": " + false +"}";
                                            publish(topic, RFCommunication, type);
                                        } else {
                                            RFCommunication ="{\"type\": " + "\"SR\"" + ",\"button\": " + swBtn + ",\"state\": " + true +"}";
                                            publish(topic, RFCommunication, type);
                                        }
                                    }

                                }

                                @Override
                                public void onLongItemClick(View view, int position) {
                                    Toast.makeText(getContext(), "Long Click", Toast.LENGTH_SHORT).show();
//                                    PopupMenu popupMenu = new PopupMenu(getContext(),rclDevices.getChildAt(position));
//                                    popupMenu.getMenuInflater().inflate(R.menu.room_menu,popupMenu.getMenu());
//                                    popupMenu.setOnMenuItemClickListener(menuItem -> {
//                                        switch (menuItem.getItemId()){
//                                            case R.id.menu_delete:{
//                                                deleteDevice(SharedPreferencesUtils.loadToken(getContext())
//                                                        ,devicesArrayList.get(position).getId());
//                                                break;
//                                            }
//                                        }
//                                        return false;
//                                    });
//                                    popupMenu.show();
                                }
                            })
                    );
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
        tvRoomName = view.findViewById(R.id.tv_room_name);
        rclDevices = view.findViewById(R.id.rclDevices);

        tv_humidity = view.findViewById(R.id.humidity);
        tv_temperature = view.findViewById(R.id.temperature);

        imgRefresh = view.findViewById(R.id.img_refresh);
        imgAddDevice = view.findViewById(R.id.img_add_device);

        devicesArrayList = new ArrayList<>();
        devicesAdapter = new DevicesAdapter(new ArrayList<Devices>(), getContext());

        rclDevices.setAdapter(devicesAdapter);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        rclDevices.setLayoutManager(mLayoutManager);

        imgRefresh.setOnClickListener(this);
        imgAddDevice.setOnClickListener(this);

        loadingDialog = new ACProgressPie.Builder(getContext())
                .ringColor(Color.WHITE)
                .pieColor(Color.WHITE)
                .updateType(ACProgressConstant.PIE_AUTO_UPDATE)
                .build();
        loadingDialog.setCancelable(true);

        //mqtt
        clientId = MqttClient.generateClientId();

    }

    public void deleteDevice(String token, String deviceId) {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiRetrofit.URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        deviceUtils = retrofit.create(DeviceUtils.class);

        Call<DeleteResponse> call = deviceUtils.deleteDevices(token, deviceId);
        call.enqueue(new Callback<DeleteResponse>() {
            @Override
            public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response) {
                DeleteResponse deleteResponse;
                deleteResponse = response.body();
                if (deleteResponse.isSuccess()) {
                    CustomToast.makeText(getContext(), deleteResponse.getMessage(), CustomToast.LENGTH_SHORT, CustomToast.SUCCESS, false).show();
                } else {
                    CustomToast.makeText(getContext(), deleteResponse.getMessage(), CustomToast.LENGTH_SHORT, CustomToast.WARNING, false).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Can't load data, please try again!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getDataDevices(String token, String idRoom) {
        loadingDialog.show();
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiRetrofit.URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        deviceUtils = retrofit.create(DeviceUtils.class);

        Call<ArrayList<Devices>> call = deviceUtils.getDevices(token, idRoom);
        call.enqueue(new Callback<ArrayList<Devices>>() {
            @Override
            public void onResponse(Call<ArrayList<Devices>> call, Response<ArrayList<Devices>> response) {
                ArrayList<Devices> devices = new ArrayList<>();
                devices = response.body();
                if (devices != null) {
                    loadingDialog.dismiss();
                    devicesArrayList = devices;
                    devicesAdapter.updateList(devices);
                    devicesAdapter.notifyDataSetChanged();

                    CustomToast.makeText(getContext(), "Let's start!", CustomToast.LENGTH_LONG, CustomToast.SUCCESS, false).show();
                } else {
                    loadingDialog.dismiss();
                    CustomToast.makeText(getContext(), "Fail", CustomToast.LENGTH_LONG, CustomToast.CONFUSING, false).show();
                }

            }

            @Override
            public void onFailure(Call<ArrayList<Devices>> call, Throwable t) {
                loadingDialog.dismiss();
                CustomToast.makeText(getContext(), "Failure", CustomToast.LENGTH_LONG, CustomToast.WARNING, false).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_refresh: {
                getDataDevices(SharedPreferencesUtils.loadToken(getContext()), idRoom);
                break;
            }
            case R.id.img_add_device: {
                listener.DeviceToProducts(idRoom);
                break;
            }
        }
    }

    public void publish(String topic, String payload, String type) {
        try {
            if (mqttAndroidClient.isConnected() == false) {
                mqttAndroidClient.connect();
            }

            MqttMessage message = new MqttMessage();
            message.setPayload(payload.getBytes());
            message.setQos(2);
            if (type.equals("SW2") || type.equals("SW3")) {
                message.setRetained(true);
            } else {
                message.setRetained(false);
            }

            mqttAndroidClient.publish(topic, message, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i("debug", "topic "+topic+" mess:"+message);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i("debug", "publish failed!");
                }
            });
        } catch (MqttException e) {
            Log.e("hello", e.toString());
            e.printStackTrace();
        }
    }


}
