package com.example.chikaapp.fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chikaapp.R;
import com.example.chikaapp.SharedPreferencesUtils;
import com.example.chikaapp.action.CustomToast;
import com.example.chikaapp.api.ApiRetrofit;
import com.example.chikaapp.api.DeviceUtils;
import com.example.chikaapp.model.Devices;
import com.example.chikaapp.request.CreateDeviceRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
public class AddIRDeviceFragment extends Fragment implements View.OnClickListener {

    ImageView imgAirConditioner, imgTelevision, imgPickAirCond, imgPickTv;
    Button btnDone;
    EditText edtNameDevice;
    CommunicationInterface listener;
    String idRoom, type, topic, logo;
    DeviceUtils deviceUtils;
    ACProgressPie dialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CommunicationInterface) {
            listener = (CommunicationInterface) context;
        } else {
            throw new RuntimeException(context.toString() + "Can phai implement");
        }
    }

    public AddIRDeviceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_ir_device, container, false);
        initialize(view);
        Bundle bundle = getArguments();
        idRoom = bundle.getString("idRoom");
        type = bundle.getString("type");
        topic = bundle.getString("topic");
        return view;
    }

    public void initialize(View view) {
        imgAirConditioner = view.findViewById(R.id.img_air_cond);
        imgTelevision = view.findViewById(R.id.img_tv);
        imgPickAirCond = view.findViewById(R.id.img_tick);
        imgPickTv = view.findViewById(R.id.img_tick1);
        btnDone = view.findViewById(R.id.btn_Done);
        edtNameDevice = view.findViewById(R.id.edt_device_name);

        imgAirConditioner.setOnClickListener(this);
        imgTelevision.setOnClickListener(this);
        btnDone.setOnClickListener(this);

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
            case R.id.img_air_cond:
                if (imgPickAirCond.getVisibility() == View.GONE) {
                    if (imgPickTv.getVisibility() == View.VISIBLE) {
                        imgPickTv.setVisibility(View.GONE);
                    }
                    imgPickAirCond.setVisibility(View.VISIBLE);
                } else {
                    imgPickAirCond.setVisibility(View.GONE);
                }
                break;
            case R.id.img_tv:
                if (imgPickTv.getVisibility() == View.GONE) {
                    if (imgPickAirCond.getVisibility() == View.VISIBLE) {
                        imgPickAirCond.setVisibility(View.GONE);
                    }
                    imgPickTv.setVisibility(View.VISIBLE);
                }
            case R.id.btn_Done:
                if (((imgPickTv.getVisibility() == View.VISIBLE) || imgPickAirCond.getVisibility() == View.VISIBLE) && !edtNameDevice.getText().toString().isEmpty()) {
                    if (imgPickTv.getVisibility() == View.VISIBLE) {
                        logo = "television";
                    } else
                        logo = "air-conditioner";
                    CreateDeviceRequest request = new CreateDeviceRequest(logo, edtNameDevice.getText().toString(),idRoom, 0,topic, type);
                    createIRDevice(SharedPreferencesUtils.loadToken(getContext()),request);
                } else
                    CustomToast.makeText(getContext()
                            , "Please pick logo and input name of device!"
                            , CustomToast.LENGTH_LONG
                            , CustomToast.CONFUSING
                            , false).show();

                break;
            default:
                break;
        }
    }

    public void createIRDevice(String token, CreateDeviceRequest createDeviceRequest) {
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
                dialog.dismiss();
                if (devices != null) {
                    CustomToast.makeText(getContext(), "Create Success", CustomToast.LENGTH_LONG, CustomToast.SUCCESS, false).show();
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

    }
}
