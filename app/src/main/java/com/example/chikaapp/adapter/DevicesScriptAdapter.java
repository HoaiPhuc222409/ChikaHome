package com.example.chikaapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chikaapp.R;
import com.example.chikaapp.model.DeviceScript;

import java.util.ArrayList;
import java.util.HashSet;

public class DevicesScriptAdapter extends RecyclerView.Adapter<DevicesScriptAdapter.ViewHolder> {
    ArrayList<DeviceScript> devicesArrayList;
    ArrayList<DeviceScript> devicesActiveArrayList = new ArrayList<>();
    int imgId;
    Context context;
    static boolean status = false;
    DeviceScript devices;

    public DevicesScriptAdapter(ArrayList<DeviceScript> devicesArrayList, Context context) {
        this.devicesArrayList = devicesArrayList;
        this.context = context;
    }

    public void updateList(ArrayList<DeviceScript> devicesArrayList) {
        this.devicesArrayList = devicesArrayList;
    }

    @NonNull
    @Override
    public DevicesScriptAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_devices_active, parent, false);
        DevicesScriptAdapter.ViewHolder viewHolder = new DevicesScriptAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DevicesScriptAdapter.ViewHolder holder, int position) {
        devices = devicesArrayList.get(position);
        holder.img_devices.setImageResource(getDevicesImage(devices.getName().toLowerCase()));
        holder.tv_device_name.setText(devices.getName());
        holder.text.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return devicesArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
        ImageView img_devices;
        Switch aSwitch;
        TextView tv_device_name, tv_status, text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            text = itemView.findViewById(R.id.tv_text);
            aSwitch = itemView.findViewById(R.id.sw);
            aSwitch.setOnCheckedChangeListener(this);

            img_devices = itemView.findViewById(R.id.img_devices_active);
            tv_device_name = itemView.findViewById(R.id.tv_device_active_name);
            tv_status = itemView.findViewById(R.id.tv_status_device);
        }

        @Override
        public void onClick(View view) {
            aSwitch.setVisibility(View.VISIBLE);

            if (aSwitch.isChecked()) {
                aSwitch.setChecked(false);

            } else {
                aSwitch.setChecked(true);
                status = true;
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
                tv_status.setText("Mở");
                devices.setState(true);
            } else {
                tv_status.setText("Tắt");
                devices.setState(false);
            }
            devicesActiveArrayList.add(devices);
            solveDuplicate(devicesActiveArrayList);
        }
    }

    public void solveDuplicate(ArrayList<DeviceScript> deviceScriptArrayList) {
        HashSet<DeviceScript> hashSet = new HashSet<DeviceScript>();
        hashSet.addAll(deviceScriptArrayList);
        deviceScriptArrayList.clear();
        deviceScriptArrayList.addAll(hashSet);
    }

    public int getDevicesImage(String roomImageName) {
        switch (roomImageName) {
            case "đèn trần": {
                imgId = R.drawable.light_icon;
                break;
            }
            case "đèn chùm": {
                imgId = R.drawable.chandelier_icon;
                break;
            }
            case "quạt": {
                imgId = R.drawable.ac_fan_icon;
                break;
            }
            case "air": {
                imgId = R.drawable.ca_ss03;
                break;
            }
            case "television": {
                imgId = R.drawable.television_icon;
                break;
            }
            case "rèm cửa": {
                imgId = R.drawable.curtain_icon;
                break;
            }
            case "cửa":
                imgId = R.drawable.door_close_icon;
                break;
            case "loa":
                imgId = R.drawable.speaker_icon;
                break;
            case "máy lạnh":
                imgId = R.drawable.air_conditioner_icon;
                break;
            case "đèn ngủ":
                imgId = R.drawable.lamp_icon;
                break;
            case "đèn vườn":
                imgId = R.drawable.garden_light_icon;
                break;
            case "quạt trần":
                imgId = R.drawable.selling_fan_icon;
                break;
            case "create":
                imgId = R.drawable.ic_add_circle;
                break;
            default:
                imgId = R.drawable.bg_button;
        }
        return imgId;
    }
}
