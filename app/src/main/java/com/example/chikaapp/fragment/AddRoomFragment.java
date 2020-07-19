package com.example.chikaapp.fragment;


import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chikaapp.R;
import com.example.chikaapp.SharedPreferencesUtils;
import com.example.chikaapp.action.CustomToast;
import com.example.chikaapp.adapter.ImageAdapter;
import com.example.chikaapp.api.RoomUtils;
import com.example.chikaapp.api.ApiRetrofit;
import com.example.chikaapp.model.Image;
import com.example.chikaapp.model.Room;
import com.example.chikaapp.request.CreateRoomRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressPie;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddRoomFragment extends Fragment implements View.OnClickListener{

    ImageView img_Add;
    ImageView img_Room;
    EditText edt_Room_Name;
    TextView tvDone;

    RoomUtils roomUtils;

    public int room_url_image;
    public String room_name;

    Image logo=new Image();
    GridView gridView;

    ACProgressPie dialog;

    public AddRoomFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_room, container, false);
        initialize(view);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = gridView.getItemAtPosition(position);
                Image image = (Image) o;
                logo=image;
                img_Room.setImageResource(image.getId());
            }
        });

        return view;
    }

    public void initialize(View view){
        img_Room=view.findViewById(R.id.img_icon_room);
        edt_Room_Name=view.findViewById(R.id.edt_room_name);
        tvDone=view.findViewById(R.id.tvDone);

        tvDone.setOnClickListener(this);

        List<Image> image_details = getListData();

        gridView = view.findViewById(R.id.gridImage);
        gridView.setAdapter(new ImageAdapter(getContext(), image_details));

        dialog = new ACProgressPie.Builder(getContext())
                .ringColor(Color.WHITE)
                .pieColor(Color.WHITE)
                .updateType(ACProgressConstant.PIE_AUTO_UPDATE)
                .build();
        dialog.setCancelable(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvDone:{
                createRoom();
                break;
            }
        }
    }

    private List<Image> getListData() {
        List<Image> list = new ArrayList<>();

        list.add(new Image("living-room", R.drawable.icon_living_room));
        list.add(new Image("bedroom", R.drawable.icon_bed_room));
        list.add(new Image("garden", R.drawable.icon_garden));
        list.add(new Image("kitchen", R.drawable.kitchen_icon));
        list.add(new Image("working-room",R.drawable.icon_working_room));
        list.add(new Image("bathroom",R.drawable.icon_bath_room));
        list.add(new Image("garage",R.drawable.icon_garage));
        return list;
    }



    public void createRoom(){
        if (!edt_Room_Name.getText().toString().equals("")){
            room_name=edt_Room_Name.getText().toString();

            dialog.show();
            Gson gson = new GsonBuilder().setLenient().create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiRetrofit.URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            roomUtils = retrofit.create(RoomUtils.class);
            Call<Room> call = roomUtils.createRoom(SharedPreferencesUtils.loadToken(getContext()),
                    new CreateRoomRequest(logo.getName_image(),room_name));
            call.enqueue(new Callback<Room>() {
                @Override
                public void onResponse(Call<Room> call, Response<Room> response) {
                    Room room = response.body();
                    if (room != null) {
                        dialog.dismiss();
                        CustomToast.makeText(getContext(),"Create Success",CustomToast.LENGTH_LONG,CustomToast.SUCCESS,false).show();
                        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();

                        fragmentTransaction.replace(R.id.frame_container,new RoomFragment());
                        fragmentTransaction.commit();

                    } else {
                        CustomToast.makeText(getContext(),"Create Fail",CustomToast.LENGTH_LONG,CustomToast.WARNING,false).show();
                    }
                }

                @Override
                public void onFailure(Call<Room> call, Throwable t) {
                    dialog.dismiss();
                    CustomToast.makeText(getContext(),""+t.toString(),CustomToast.LENGTH_LONG,CustomToast.WARNING,false).show();
                }


            });

        } else {
            Toast.makeText(getContext(), "Please input your room's name", Toast.LENGTH_SHORT).show();
        }
    }


}
