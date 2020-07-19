package com.example.chikaapp.adapter;

import android.content.Context;
import android.renderscript.Script;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chikaapp.R;
import com.example.chikaapp.model.Room;
import com.example.chikaapp.model.Scripts;

import java.util.ArrayList;

public class ScriptsAdapter extends RecyclerView.Adapter<ScriptsAdapter.ViewHolder> {
    ArrayList<Scripts> scriptsArrayList;
    Context context;
    int imgId;

    public ScriptsAdapter(ArrayList<Scripts> scriptsArrayList, Context context) {
        this.scriptsArrayList = scriptsArrayList;
        this.context = context;
    }

    public void updateList(ArrayList<Scripts> scriptsArrayList) {
        this.scriptsArrayList = scriptsArrayList;
    }

    @NonNull
    @Override
    public ScriptsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_script, parent, false);
        ViewHolder viewHolder = new ScriptsAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Scripts scripts=scriptsArrayList.get(position);
        holder.tv_script_name.setText(scripts.getName());
        holder.tv_time.setText(scripts.getTime());
        holder.img_logo.setImageResource(getScriptsImage(scripts.getLogo()));
        String dayInWeek = scripts.getDays();
        try {
            if (dayInWeek.equals("MON,TUE,WED,THU,FRI,SAT,SUN")){
                holder.tv_monday.setVisibility(View.VISIBLE);
                holder.tv_monday.setText("MỖI NGÀY");
            } else {
                if (dayInWeek.contains("MON")){
                    holder.tv_monday.setVisibility(View.VISIBLE);
                }
                if (dayInWeek.contains("TUE")){
                    holder.tv_tuesday.setVisibility(View.VISIBLE);
                }
                if (dayInWeek.contains("WED")){
                    holder.tv_wednesday.setVisibility(View.VISIBLE);
                }
                if (dayInWeek.contains("THU")){
                    holder.tv_thursday.setVisibility(View.VISIBLE);
                }
                if (dayInWeek.contains("FRI")){
                    holder.tv_friday.setVisibility(View.VISIBLE);
                }
                if (dayInWeek.contains("SAT")){
                    holder.tv_saturday.setVisibility(View.VISIBLE);
                }
                if (dayInWeek.contains("SUN")){
                    holder.tv_sunday.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e){
            Log.i("fuck", e.toString());
        }

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
    public int getItemCount() {
        return scriptsArrayList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_logo;
        TextView tv_script_name, tv_time, tv_monday, tv_tuesday, tv_wednesday, tv_thursday, tv_friday, tv_saturday, tv_sunday;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_logo = itemView.findViewById(R.id.img_scripts);
            tv_script_name = itemView.findViewById(R.id.tv_script_name);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_monday = itemView.findViewById(R.id.tvMonday);
            tv_tuesday = itemView.findViewById(R.id.tvTuesday);
            tv_wednesday = itemView.findViewById(R.id.tvWednesday);
            tv_thursday = itemView.findViewById(R.id.tvThursday);
            tv_friday = itemView.findViewById(R.id.tvFriday);
            tv_saturday = itemView.findViewById(R.id.tvSaturday);
            tv_sunday  = itemView.findViewById(R.id.tvSunday);
        }
    }
}
