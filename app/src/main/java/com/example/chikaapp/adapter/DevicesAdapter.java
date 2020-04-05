package com.example.chikaapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chikaapp.R;
import com.example.chikaapp.model.Devices;
import com.example.chikaapp.model.Room;

import java.util.ArrayList;

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.ViewHolder> {

    ArrayList<Devices> devicesArrayList;
    Context context;

    public DevicesAdapter(ArrayList<Devices> devicesArrayList, Context context) {
        this.devicesArrayList = devicesArrayList;
        this.context = context;
    }

    public void updateList(ArrayList<Devices> devicesArrayList) {
        this.devicesArrayList = devicesArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_device, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Devices devices = devicesArrayList.get(position);
        holder.tv_device_name.setText(devices.getName());
    }

    @Override
    public int getItemCount() {
        return devicesArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_device_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_device_name = itemView.findViewById(R.id.tv_devices_name);
        }
    }
}
