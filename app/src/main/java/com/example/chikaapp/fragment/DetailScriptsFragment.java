package com.example.chikaapp.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.core.app.NavUtils;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chikaapp.R;
import com.example.chikaapp.SharedPreferencesUtils;
import com.example.chikaapp.action.CustomToast;
import com.example.chikaapp.adapter.DevicesActiveAdapter;
import com.example.chikaapp.api.ApiRetrofit;
import com.example.chikaapp.api.RoomUtils;
import com.example.chikaapp.api.ScriptUtils;
import com.example.chikaapp.model.DeleteResponse;
import com.example.chikaapp.model.DeviceActive;
import com.example.chikaapp.model.Devices;
import com.example.chikaapp.model.Scripts;
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
public class DetailScriptsFragment extends Fragment implements View.OnClickListener{

    TextView tv_Name, tv_t2, tv_t3, tv_t4, tv_t5, tv_t6, tv_t7, tv_cn, tv_Time;
    ImageView img_logo;
    Button btn_delete, btn_back;
    RecyclerView rcl_devices;
    ArrayList<DeviceActive> devicesArrayList;
    DevicesActiveAdapter devicesActiveAdapter;
    ScriptUtils scriptUtils;
    String scriptId;
    int imgId;


    public DetailScriptsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_detail_scripts, container, false);
        initialize(view);
        Bundle bundle = getArguments();
        Scripts scripts=(Scripts) bundle.getSerializable("scripts");

        tv_Name.setText(scripts.getName());
        tv_Time.setText(scripts.getTime());
        img_logo.setImageResource(getScriptsImage(scripts.getLogo()));
        scriptId = scripts.getId();

        devicesArrayList = scripts.getDevices();
        devicesActiveAdapter.updateList(devicesArrayList);
        devicesActiveAdapter.notifyDataSetChanged();

        String dayInWeek=scripts.getDays();
        try {
            if (dayInWeek.equals("MON,TUE,WED,THU,FRI,SAT,SUN")){
                tv_t2.setVisibility(View.VISIBLE);
                tv_t2.setText("MỖI NGÀY");
            } else {
                if (dayInWeek.contains("MON")){
                    tv_t2.setVisibility(View.VISIBLE);
                }
                if (dayInWeek.contains("TUE")){
                    tv_t3.setVisibility(View.VISIBLE);
                }
                if (dayInWeek.contains("WED")){
                    tv_t4.setVisibility(View.VISIBLE);
                }
                if (dayInWeek.contains("THU")){
                    tv_t5.setVisibility(View.VISIBLE);
                }
                if (dayInWeek.contains("FRI")){
                    tv_t6.setVisibility(View.VISIBLE);
                }
                if (dayInWeek.contains("SAT")){
                    tv_t7.setVisibility(View.VISIBLE);
                }
                if (dayInWeek.contains("SUN")){
                    tv_cn.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e){
            Log.i("fuck", e.toString());
        }
        return view;
    }

    public void initialize(View view){
        tv_t2 = view.findViewById(R.id.tvMonday);
        tv_t3 = view.findViewById(R.id.tvTuesday);
        tv_t4 = view.findViewById(R.id.tvWednesday);
        tv_t5 = view.findViewById(R.id.tvThursday);
        tv_t6 = view.findViewById(R.id.tvFriday);
        tv_t7 = view.findViewById(R.id.tvSaturday);
        tv_cn = view.findViewById(R.id.tvSunday);
        tv_Name = view.findViewById(R.id.tv_script_name);
        tv_Time = view.findViewById(R.id.tv_time);
        img_logo = view.findViewById(R.id.img_scripts);
        btn_back = view.findViewById(R.id.btn_Back);
        btn_delete = view.findViewById(R.id.btn_Delete);

        btn_back.setOnClickListener(this);
        btn_delete.setOnClickListener(this);

        devicesArrayList = new ArrayList<>();
        devicesActiveAdapter = new DevicesActiveAdapter(new ArrayList<>(),getContext());

        rcl_devices = view.findViewById(R.id.rclDevicesActive);
        rcl_devices.setAdapter(devicesActiveAdapter);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(),1);
        rcl_devices.setLayoutManager(mLayoutManager);
    }

    public int getScriptsImage(String scriptsImageName) {
        switch (scriptsImageName) {
            case "go-work": {
                imgId = R.drawable.ic_go_work;
                break;
            }
            case "play-music":{
                imgId = R.drawable.ic_play_music;
                break;
            }
            case "go-sleep": {
                imgId = R.drawable.ic_go_sleep;
                break;
            }
            case "water-plant":
                imgId=R.drawable.ic_water_plant;
                break;
            case "wake-up":
                imgId = R.drawable.ic_wake_up;
                break;
            case "security":
                imgId = R.drawable.ic_security;
                break;
            case "eat":
                imgId = R.drawable.ic_eat;
                break;
            case "create":
                imgId=R.drawable.ic_add_circle;
                break;
            default: imgId = R.drawable.bg_button;
        }
        return imgId;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_Back:
                showScriptFragment();
                break;
            case R.id.btn_Delete:
                new AlertDialog.Builder(getContext())
                        .setTitle("Xóa Kịch Bản")
                        .setMessage("Bạn có chắc muốn xóa kịch bản này?")


                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            deleteScript(SharedPreferencesUtils.loadToken(getContext()),scriptId);
                            Toast.makeText(getContext(), ""+scriptId, Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                break;
        }
    }

    public void deleteScript(String token, String scriptId){
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiRetrofit.URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
         scriptUtils= retrofit.create(ScriptUtils.class);

        Call<DeleteResponse> call = scriptUtils.deleteScript(token, scriptId);
        call.enqueue(new Callback<DeleteResponse>() {
            @Override
            public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response) {
                DeleteResponse deleteResponse;
                deleteResponse = response.body();
                if (deleteResponse!=null){
                    if (deleteResponse.isSuccess()){
                        CustomToast.makeText(getContext(), deleteResponse.getMessage(), CustomToast.LENGTH_SHORT,CustomToast.SUCCESS,false).show();
                        showScriptFragment();
                    } else {
                        CustomToast.makeText(getContext(), deleteResponse.getMessage(), CustomToast.LENGTH_SHORT,CustomToast.ERROR,false).show();
                    }
                } else {
                    CustomToast.makeText(getContext(),"Please try later", CustomToast.LENGTH_LONG, CustomToast.ERROR,false).show();
                }

            }

            @Override
            public void onFailure(Call<DeleteResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Can't load data, please try again!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showScriptFragment(){
        ScriptFragment fragment = new ScriptFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
