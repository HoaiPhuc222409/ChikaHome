package com.example.chikaapp.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chikaapp.R;
import com.example.chikaapp.action.RecyclerItemClickListener;
import com.example.chikaapp.adapter.DevicesScriptAdapter;
import com.example.chikaapp.adapter.ImageAdapter;
import com.example.chikaapp.fragment.AddDevicesToScriptFragment;
import com.example.chikaapp.fragment.CommunicationInterface;
import com.example.chikaapp.model.DeviceScript;
import com.example.chikaapp.model.Image;
import com.example.chikaapp.model.ScriptDevices;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DeviceScriptDialog extends DialogFragment {
    RecyclerView rclDevice;
    TextView btnDone;
    DeviceScript deviceScript;
    CommunicationInterface listener;
    DevicesScriptAdapter devicesScriptAdapter;
    ArrayList<DeviceScript> deviceScripts;
    ArrayList<DeviceScript> deviceToCreateScript;

    public static DeviceScriptDialog newInStance(ArrayList<DeviceScript> deviceScriptArrayList) {
        DeviceScriptDialog imageScriptDialog = new DeviceScriptDialog();
        Bundle args = new Bundle();
        args.putSerializable("deviceScript", (Serializable) deviceScriptArrayList);
        imageScriptDialog.setArguments(args);
        return imageScriptDialog;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_device, container);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rclDevice = view.findViewById(R.id.rcl_dialog_device);
        btnDone = view.findViewById(R.id.tvDone);

        btnDone.setOnClickListener(v->{
            Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("device_dialog");
            if (prev != null) {
                DialogFragment df = (DialogFragment) prev;
                df.dismiss();
            }
        });

        deviceScripts = (ArrayList<DeviceScript>) getArguments().getSerializable("deviceScript");
        if (!deviceScripts.isEmpty()) {
            devicesScriptAdapter = new DevicesScriptAdapter(deviceScripts, getContext());
            devicesScriptAdapter.notifyDataSetChanged();
            rclDevice.setAdapter(devicesScriptAdapter);
            RecyclerView.LayoutManager mLayoutManager
                    = new GridLayoutManager(getContext(), 1);
            rclDevice.setLayoutManager(mLayoutManager);
        }


        deviceToCreateScript = new ArrayList<>();

//        rclDevice.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), rclDevice, new RecyclerItemClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                if (deviceScripts.get(position).isState()){
//                    deviceScripts.get(position).setState(false);
//                } else {
//                    deviceScripts.get(position).setState(true);
//                }
//
//                Toast.makeText(getContext(), ""+deviceScripts.get(position).isState(), Toast.LENGTH_SHORT).show();
//            }
//            @Override
//            public void onLongItemClick(View view, int position) {
//
//            }
//        }));
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
