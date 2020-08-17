package com.example.chikaapp.fragment;


import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.chikaapp.R;
import com.example.chikaapp.dialog.ImageScriptDialog;
import com.example.chikaapp.model.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddScriptFragment extends Fragment {

    ImageView imgScript, imgTimer;
    List<Image> imageList;
    TextView tv_time,tv_day,tv_done, tv_mon, tv_tue, tv_wed, tv_thu, tv_fri, tv_sat, tv_sun;
    String days = "";
    boolean is24HView = true;
    private int lastSelectedHour = -1;
    private int lastSelectedMinute = -1;

    public AddScriptFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_script, container, false);

        initialize(view);

        if ((Image) getArguments().getSerializable("image") != null) {
            Image image = (Image) getArguments().getSerializable("image");
            imgScript.setImageResource(image.getId());
        }
        imageList = getListData();

        imgScript.setOnClickListener(v -> {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            ImageScriptDialog userInfoDialog = ImageScriptDialog.newInStance(imageList);
            userInfoDialog.show(fm, "image_dialog");
        });

        setDays(tv_mon);
        setDays(tv_tue);
        setDays(tv_wed);
        setDays(tv_thu);
        setDays(tv_fri);
        setDays(tv_sat);
        setDays(tv_sun);

        imgTimer.setOnClickListener(v->{
            showTimePicker();
        });

        tv_done.setOnClickListener(v->{
            for (int i=1; i<=7; i++){

            }
        });



        return view;
    }

    public void initialize(View view){
        imgScript = view.findViewById(R.id.img_scripts);
        imgTimer = view.findViewById(R.id.img_timer);
        tv_time = view.findViewById(R.id.tv_time);
        tv_day = view.findViewById(R.id.tv_day);
        tv_done = view.findViewById(R.id.tv_done);

        tv_mon = view.findViewById(R.id.tv_mon);
        tv_tue = view.findViewById(R.id.tv_tue);
        tv_wed = view.findViewById(R.id.tv_wed);
        tv_thu = view.findViewById(R.id.tv_thu);
        tv_fri = view.findViewById(R.id.tv_fri);
        tv_sat = view.findViewById(R.id.tv_sat);
        tv_sun = view.findViewById(R.id.tv_sun);
    }

    public void setDays(TextView tv){
        tv.setOnClickListener(v->{
            if (tv.getBackground().getConstantState()==getResources().getDrawable(R.drawable.bg_stroke).getConstantState()){
                tv.setBackground(getResources().getDrawable(R.drawable.bg_stroke_picked));
                tv.setTextColor(getResources().getColor(R.color.white));
            } else {
                tv.setBackground(getResources().getDrawable(R.drawable.bg_stroke));
                tv.setTextColor(getResources().getColor(R.color.black));
            }
        });
    }

    public void showTimePicker() {
        // Time Set Listener.
        TimePickerDialog.OnTimeSetListener timeSetListener = (view, hourOfDay, minute) -> {
            String stringHour, stringMinute;

            if (hourOfDay<=9){
                stringHour = "0"+hourOfDay;
                if (minute<=9){
                    stringMinute = "0"+minute;
                    tv_time.setText(stringHour + ":" + stringMinute);}
                else {
                    tv_time.setText(stringHour+":"+minute);
                }

            } else {
                if (minute<=9){
                    stringMinute = "0"+minute;
                    tv_time.setText(hourOfDay+":"+stringMinute);
                } else {
                    tv_time.setText(hourOfDay+":"+minute);
                }
            }
            lastSelectedHour = hourOfDay;
            lastSelectedMinute = minute;
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                timeSetListener, lastSelectedHour, lastSelectedMinute, is24HView);

        timePickerDialog.show();
    }

    private List<Image> getListData() {
        List<Image> list = new ArrayList<>();
        list.add(new Image("go-work", R.drawable.ic_go_work));
        list.add(new Image("go-sleep", R.drawable.ic_go_sleep));
        list.add(new Image("wake-up", R.drawable.ic_wake_up));
        list.add(new Image("go-school", R.drawable.ic_go_school));
        list.add(new Image("play-music", R.drawable.ic_play_music));
        list.add(new Image("water-plan", R.drawable.ic_water_plant));
        list.add(new Image("eat", R.drawable.ic_eat));
        return list;
    }

}
