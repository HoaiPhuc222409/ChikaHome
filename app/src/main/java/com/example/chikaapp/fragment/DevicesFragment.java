package com.example.chikaapp.fragment;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chikaapp.R;
import com.example.chikaapp.SharedPreferencesUtils;
import com.example.chikaapp.adapter.DevicesAdapter;
import com.example.chikaapp.api.ApiRetrofit;
import com.example.chikaapp.api.DeviceUtils;
import com.example.chikaapp.model.Devices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class DevicesFragment extends Fragment {

    String data;
    DeviceUtils deviceUtils;
    TextView tvRoomName;
    DevicesAdapter devicesAdapter;
    Context context;
    ArrayList<Devices> devicesArrayList;
    RecyclerView rclDevicesList;

    public DevicesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_devices, container, false);

        initialize(view);

        Bundle bundle=getArguments();
        if (bundle!=null){
            data=bundle.getString("idRoom");
            tvRoomName.setText(bundle.getString("roomName"));
            getDataDevices(SharedPreferencesUtils.loadToken(getContext()),data);
            rclDevicesList.setAdapter(devicesAdapter);
            rclDevicesList.setLayoutManager(new LinearLayoutManager(getContext()));

        } else {
            Toast.makeText(getContext(), "Can't receive data!", Toast.LENGTH_SHORT).show();
        }
        return view;


    }

    public void initialize(View view){
        devicesArrayList=new ArrayList<>();
        devicesAdapter=new DevicesAdapter(new ArrayList<Devices>(),getContext());

        tvRoomName = view.findViewById(R.id.tv_room_name);
        rclDevicesList = view.findViewById(R.id.rclDevices);
    }

    public void getDataDevices(String token, String idRoom) {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiRetrofit.URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
         deviceUtils = retrofit.create(DeviceUtils.class);

        Call<ArrayList<Devices>> call = deviceUtils.getDevices(token,idRoom);
        call.enqueue(new Callback<ArrayList<Devices>>() {
            @Override
            public void onResponse(Call<ArrayList<Devices>> call, Response<ArrayList<Devices>> response) {
                ArrayList<Devices> devices=new ArrayList<>();
                devices = response.body();
                if (devices!=null){
                    devicesArrayList=devices;
                    devicesAdapter.updateList(devices);
                    devicesAdapter.notifyDataSetChanged();

                    Toast.makeText(getContext(), "Get Device Success", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), "Can't load", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ArrayList<Devices>> call, Throwable t) {
                Toast.makeText(getContext(), "Can't load data, please try again!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
