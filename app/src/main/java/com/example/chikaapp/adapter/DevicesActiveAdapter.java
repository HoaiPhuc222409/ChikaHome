package com.example.chikaapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chikaapp.R;
import com.example.chikaapp.model.DeviceActive;
import com.example.chikaapp.model.Devices;

import java.util.ArrayList;

public class DevicesActiveAdapter extends RecyclerView.Adapter<DevicesActiveAdapter.ViewHolder> {

    ArrayList<DeviceActive> devicesArrayList;
    int imgId;
    Context context;

    public DevicesActiveAdapter(ArrayList<DeviceActive> devicesArrayList, Context context) {
        this.devicesArrayList = devicesArrayList;
        this.context = context;
    }

    public void updateList(ArrayList<DeviceActive> devicesArrayList) {
        this.devicesArrayList = devicesArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_devices_active, parent, false);
        DevicesActiveAdapter.ViewHolder viewHolder = new DevicesActiveAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DeviceActive devices = devicesArrayList.get(position);
        String status;

        holder.img_devices.setImageResource(getDevicesImage(devices.getName().toLowerCase()));
        holder.tv_device_name.setText(devices.getName());
        if (devices.isState()){
            status = "MỞ";
        } else {
            status ="TẮT";
        }
        holder.tv_status.setText(status);
    }

    @Override
    public int getItemCount() {
        return devicesArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img_devices;
        TextView tv_device_name, tv_status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_devices = itemView.findViewById(R.id.img_devices_active);
            tv_device_name = itemView.findViewById(R.id.tv_device_active_name);
            tv_status = itemView.findViewById(R.id.tv_status_device);
        }
    }

    public int getDevicesImage(String roomImageName) {
        switch (roomImageName) {
            case "đèn trần": {
                imgId = R.drawable.light_icon;
                break;
            }
            case "đèn chùm":{
                imgId = R.drawable.chandelier_icon;
                break;
            }
            case "quạt": {
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
                imgId=R.drawable.lamp_icon;
                break;
            case "đèn vườn":
                imgId=R.drawable.garden_light_icon;
                break;
            case "quạt trần":
                imgId=R.drawable.selling_fan_icon;
                break;
            case "create":
                imgId=R.drawable.ic_add_circle;
                break;
            default: imgId = R.drawable.bg_button;
        }
        return imgId;
    }
}
