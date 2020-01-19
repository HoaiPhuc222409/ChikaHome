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
import com.example.chikaapp.model.Image;
import com.example.chikaapp.model.Room;
import java.util.ArrayList;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder> {

    ArrayList<Room> roomArrayList;
    Context context;
    int logoId;


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
        Image image=new Image();
        image.setId(room.getLogo());

        switch (room.getLogo()){
            case 1:{
                logoId=R.drawable.logo_chika;
                break;
            }
            case 2:{
                logoId=R.drawable.logo_chika_full;
                break;
            }
            case 3:{
                logoId=R.id.useLogo;
            }
        }

        holder.logo.setImageResource(logoId);
        holder.room_name.setText(room.getName_Room());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView logo;
        TextView room_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            logo = itemView.findViewById(R.id.img_room);
            room_name = itemView.findViewById(R.id.tv_room_name);
        }

    }

}
