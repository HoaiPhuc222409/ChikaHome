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
import com.example.chikaapp.model.Room;

import java.util.ArrayList;

public class RoomAdapter
        extends RecyclerView.Adapter<RoomAdapter.ViewHolder> {

    ArrayList<Room> roomArrayList;
    Context context;
    public int roomId;
    public int roomImg;

    public RoomAdapter(ArrayList<Room> roomArrayList, Context context) {
        this.roomArrayList = roomArrayList;
        this.context = context;
    }

    public void updateList(ArrayList<Room> roomArrayList) {
        this.roomArrayList = roomArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent
            , int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_room, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder
            , int position) {
        Room room = roomArrayList.get(position);
        roomId= getRoomIcon(room.getLogo());
        roomImg=getBackgroundImage(room.getLogo());

        holder.img_room.setImageResource(roomId);
        holder.tv_room_name.setText(room.getName());

    }

    @Override
    public int getItemCount() {
        return roomArrayList.size();
    }

    public int getRoomIcon(String roomImageName) {
        switch (roomImageName) {
            case "living-room": {
                roomId = R.drawable.icon_living_room;
                break;
            }
            case "working-room": {
                roomId = R.drawable.icon_working_room;
                break;
            }
            case "bedroom": {
                roomId = R.drawable.icon_bed_room;
                break;
            }
            case "garden": {
                roomId = R.drawable.icon_garden;
                break;
            }
            case "kitchen": {
                roomId = R.drawable.kitchen_icon;
                break;
            }
            case "garage": {
                roomId = R.drawable.icon_garage;
                break;
            }
            case "bathroom": {
                roomId = R.drawable.icon_bath_room;
                break;
            }
        }
        return roomId;
    }

    public int getBackgroundImage(String roomImageName) {
        switch (roomImageName) {
            case "living-room": {
                roomImg = R.drawable.living_room;
                break;
            }
            case "working-room": {
                roomImg = R.drawable.working_room;
                break;
            }
            case "bedroom": {
                roomImg = R.drawable.bedroom;
                break;
            }
            case "garden": {
                roomImg = R.drawable.garden;
                break;
            }
            case "kitchen": {
                roomImg = R.drawable.kitchen;
                break;
            }
            case "garage": {
                roomImg = R.drawable.garage;
                break;
            }
            case "bathroom": {
                roomImg = R.drawable.bathroom;
                break;
            }
        }
        return roomImg;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_room;
        TextView tv_room_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_room = itemView.findViewById(R.id.img_room);
            tv_room_name = itemView.findViewById(R.id.tv_script_name);
        }
    }

}
