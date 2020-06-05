package com.example.chikaapp.fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chikaapp.R;
import com.example.chikaapp.SharedPreferencesUtils;
import com.example.chikaapp.action.CustomToast;
import com.example.chikaapp.adapter.ImageAdapter;
import com.example.chikaapp.api.ApiRetrofit;
import com.example.chikaapp.api.DeviceUtils;
import com.example.chikaapp.api.RoomUtils;
import com.example.chikaapp.model.Devices;
import com.example.chikaapp.model.Image;
import com.example.chikaapp.model.Room;
import com.example.chikaapp.request.CreateDeviceRequest;
import com.example.chikaapp.request.CreateRoomRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

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
public class AddDeviceFragment extends Fragment implements View.OnClickListener {

    GridView gridView;
    ImageView img_Device_Logo;
    EditText edt_Device_Name;
    ACProgressPie dialog;
    TextView tvDone;
    String device_name;
    CreateDeviceRequest createDeviceRequest;
    DeviceUtils deviceUtils;
    int switchButton;
    String idRoom, topic, type;
    Image logo = new Image();
    CommunicationInterface listener;

    public AddDeviceFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_device, container, false);
        initialize(view);


        Bundle bundle = getArguments();
        switchButton=bundle.getInt("swButton");
        idRoom = bundle.getString("idRoom");
        topic = bundle.getString("topic");
        type = bundle.getString("type");

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = gridView.getItemAtPosition(position);
                Image image = (Image) o;
                logo = image;
                img_Device_Logo.setImageResource(image.getId());
            }
        });

        return view;
    }

    public void initialize(View view) {

        img_Device_Logo = view.findViewById(R.id.img_logo_here);
        edt_Device_Name = view.findViewById(R.id.edt_device_name);
        tvDone = view.findViewById(R.id.tvDone);

        tvDone.setOnClickListener(this);
        img_Device_Logo.setOnClickListener(this);
        List<Image> image_details = getListData();

        gridView = view.findViewById(R.id.gridImageDevice);
        gridView.setAdapter(new ImageAdapter(getContext(), image_details));

        dialog = new ACProgressPie.Builder(getContext())
                .ringColor(Color.WHITE)
                .pieColor(Color.WHITE)
                .updateType(ACProgressConstant.PIE_AUTO_UPDATE)
                .build();
        dialog.setCancelable(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_logo_here: {
                gridView.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.tvDone:
                createDevice(SharedPreferencesUtils.loadToken(getContext()),new CreateDeviceRequest(logo.getName_image()
                        ,edt_Device_Name.getText().toString()
                        ,idRoom, switchButton, topic, type));
                break;
        }
    }

    private List<Image> getListData() {
        List<Image> list = new ArrayList<>();

        list.add(new Image("light", R.drawable.light_icon));
        list.add(new Image("chandelier", R.drawable.chandelier_icon));
        list.add(new Image("fan", R.drawable.ac_fan_icon));
        list.add(new Image("curtain", R.drawable.curtain_icon));
        list.add(new Image("lamp", R.drawable.lamp_icon));
        list.add(new Image("garden-light", R.drawable.garden_light_icon));
        list.add(new Image("selling-fan", R.drawable.selling_fan_icon));
        list.add(new Image("speaker", R.drawable.speaker_icon));
        return list;
    }


    public void createDevice(String token, CreateDeviceRequest createDeviceRequest) {
        if (!edt_Device_Name.getText().toString().equals("")) {
            device_name = edt_Device_Name.getText().toString();

            dialog.show();
            Gson gson = new GsonBuilder().setLenient().create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiRetrofit.URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            deviceUtils = retrofit.create(DeviceUtils.class);
            Call<Devices> call = deviceUtils.createDevice(token, createDeviceRequest);
            call.enqueue(new Callback<Devices>() {
                @Override
                public void onResponse(Call<Devices> call, Response<Devices> response) {
                    Devices devices = response.body();
                    if (devices != null) {
                        dialog.dismiss();
                        CustomToast.makeText(getContext(), "Create Success", CustomToast.LENGTH_LONG, CustomToast.SUCCESS, false).show();
                        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame_container,new RoomFragment());
                        fragmentTransaction.commit();
                    } else {
                        CustomToast.makeText(getContext(), "Create Fail", CustomToast.LENGTH_LONG, CustomToast.WARNING, false).show();
                    }
                }

                @Override
                public void onFailure(Call<Devices> call, Throwable t) {
                    dialog.dismiss();
                    CustomToast.makeText(getContext(), "" + t.toString(), CustomToast.LENGTH_LONG, CustomToast.WARNING, false).show();
                }


            });

        } else {
            CustomToast.makeText(getContext(), "Please input your device's name", CustomToast.LENGTH_SHORT,CustomToast.WARNING,false).show();
        }
    }

}
