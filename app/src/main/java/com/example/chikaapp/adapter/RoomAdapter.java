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
import com.example.chikaapp.model.Room;

import java.util.ArrayList;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder> {

    ArrayList<Room> roomArrayList;
    Context context;
    public int roomId;

    public RoomAdapter(ArrayList<Room> roomArrayList, Context context) {
        this.roomArrayList = roomArrayList;
        this.context = context;
    }

    public void updateList(ArrayList<Room> roomArrayList) {
        this.roomArrayList = roomArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_room, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Room room = roomArrayList.get(position);

        roomId=getRoomImage(room.getLogo());
        holder.img_room.setImageResource(roomId);
        holder.tv_room_name.setText(room.getName());
        holder.tv_date_create.setText(room.getCreateAt());
    }

    @Override
    public int getItemCount() {
        return roomArrayList.size();
    }

    public int getRoomImage(String roomName) {
        switch (roomName) {
            case "livingRoom": {
                roomId = R.drawable.bg_button;
                break;
            }
            case "bedRoom": {
                roomId = R.drawable.ic_camera;
                break;
            }
            case "garden": {
                roomId = R.drawable.bg_item;
                break;
            }
            case "kitchen": {
                roomId = R.drawable.bg_new;
                break;
            }
        }
        return roomId;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_room;
        TextView tv_room_name, tv_date_create;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_room = itemView.findViewById(R.id.img_room);
            tv_room_name = itemView.findViewById(R.id.tv_room_name);
            tv_date_create = itemView.findViewById(R.id.tvCreateAt);
        }
    }
}
