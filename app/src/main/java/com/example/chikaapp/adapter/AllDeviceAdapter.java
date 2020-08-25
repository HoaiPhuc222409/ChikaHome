package com.example.chikaapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chikaapp.R;
import com.example.chikaapp.model.ScriptDevices;

import java.util.ArrayList;

public class AllDeviceAdapter extends RecyclerView.Adapter<AllDeviceAdapter.ViewHolder> {

    ArrayList<ScriptDevices> scriptDevicesArrayList;
    Context context;

    public AllDeviceAdapter(ArrayList<ScriptDevices> scriptDevicesArrayList, Context context) {
        this.scriptDevicesArrayList = scriptDevicesArrayList;
        this.context = context;
    }

    public void updateList(ArrayList<ScriptDevices> scriptDevicesArrayList) {
        this.scriptDevicesArrayList = scriptDevicesArrayList;
    }

    @NonNull
    @Override
    public AllDeviceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(context).inflate(R.layout.item_script_devices, parent, false);
        AllDeviceAdapter.ViewHolder viewHolder = new AllDeviceAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AllDeviceAdapter.ViewHolder holder, int position) {
        ScriptDevices scriptDevices = scriptDevicesArrayList.get(position);
        holder.roomName.setText(scriptDevices.getRoomName());
        holder.numDevice.setText(scriptDevices.getDevices().size()+" thiết bị");
//            if (scriptDevices.getDevices().size()==0){
//                holder.rclDevices.setVisibility(View.GONE);
//            } else {
//                DevicesScriptAdapter devicesScriptAdapter = new DevicesScriptAdapter(scriptDevices.getDevices(), context);
//                devicesScriptAdapter.notifyDataSetChanged();
//                holder.rclDevices.setAdapter(devicesScriptAdapter);
//                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context,1);
//                holder.rclDevices.setLayoutManager(mLayoutManager);
//            }


    }

    @Override
    public int getItemCount() {
        return scriptDevicesArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView roomName, numDevice;
//        RecyclerView rclDevices;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            roomName = itemView.findViewById(R.id.tv_room_name);
            numDevice = itemView.findViewById(R.id.tv_num_device);
//            rclDevices = itemView.findViewById(R.id.rcl_script_devices);
        }
    }
}
