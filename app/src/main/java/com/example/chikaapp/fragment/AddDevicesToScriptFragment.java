package com.example.chikaapp.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.chikaapp.R;
import com.example.chikaapp.SharedPreferencesUtils;
import com.example.chikaapp.action.CustomToast;
import com.example.chikaapp.action.RecyclerItemClickListener;
import com.example.chikaapp.adapter.AllDeviceAdapter;
import com.example.chikaapp.api.ApiRetrofit;
import com.example.chikaapp.api.DeviceUtils;
import com.example.chikaapp.dialog.DeviceScriptDialog;
import com.example.chikaapp.dialog.ImageScriptDialog;
import com.example.chikaapp.model.DeviceScript;
import com.example.chikaapp.model.ScriptDevices;
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
public class AddDevicesToScriptFragment extends Fragment {

    RecyclerView rclAllDevice;
    DeviceUtils deviceUtils;
    ArrayList<ScriptDevices> deviceScripts;
    AllDeviceAdapter allDeviceAdapter;
    Button btnDone;

    public AddDevicesToScriptFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_devices_to_script, container, false);
        initialize(view);
        getAllDeviceForScript(SharedPreferencesUtils.loadToken(getContext()));
        btnDone.setOnClickListener(v->{

        });
        rclAllDevice.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), rclAllDevice, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                DeviceScriptDialog userInfoDialog = DeviceScriptDialog.newInStance(deviceScripts.get(position).getDevices());
                userInfoDialog.show(fm, "device_dialog");
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        return view;
    }

    public void initialize(View view){
        rclAllDevice = view.findViewById(R.id.rcl_all_device);
        allDeviceAdapter = new AllDeviceAdapter(new ArrayList<ScriptDevices>(),getContext());
        rclAllDevice.setAdapter(allDeviceAdapter);
        btnDone = view.findViewById(R.id.btnCreate);
        RecyclerView.LayoutManager mLayoutManager
                = new GridLayoutManager(getContext(),1);
        rclAllDevice.setLayoutManager(mLayoutManager);
        deviceScripts = new ArrayList<>();
    }

    public void getAllDeviceForScript(String token){
            Gson gson = new GsonBuilder().setLenient().create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiRetrofit.URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            deviceUtils = retrofit.create(DeviceUtils.class);
            Call<ArrayList<ScriptDevices>> call = deviceUtils.getAllDevice(token);
            call.enqueue(new Callback<ArrayList<ScriptDevices>>(){
                @Override
                public void onResponse(Call<ArrayList<ScriptDevices>> call, Response<ArrayList<ScriptDevices>> response) {
                    deviceScripts = response.body();
                    if (deviceScripts!=null){
                        allDeviceAdapter.updateList(deviceScripts);
                        allDeviceAdapter.notifyDataSetChanged();
                    } else {
                        Log.i("debug", "not receive");
                    }
                }
                @Override
                public void onFailure(Call<ArrayList<ScriptDevices>> call, Throwable t) {
                    CustomToast.makeText(getContext(), "" + t.toString(), CustomToast.LENGTH_LONG, CustomToast.WARNING, false).show();
                }


            });


    }

}
