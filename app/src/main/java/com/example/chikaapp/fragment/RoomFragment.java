package com.example.chikaapp.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chikaapp.R;
import com.example.chikaapp.SharedPreferencesUtils;
import com.example.chikaapp.action.CustomToast;
import com.example.chikaapp.action.RecyclerItemClickListener;
import com.example.chikaapp.activity.LoginActivity;
import com.example.chikaapp.activity.MainActivity;
import com.example.chikaapp.adapter.RoomAdapter;
import com.example.chikaapp.api.ApiRetrofit;
import com.example.chikaapp.api.RoomUtils;
import com.example.chikaapp.model.DeleteRespones;
import com.example.chikaapp.model.Devices;
import com.example.chikaapp.model.Room;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressPie;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoomFragment extends Fragment implements View.OnClickListener {

    TextView btnAdd, tvNoRoom;
    RecyclerView rclRoomList;

    RoomUtils roomUtils;
    RoomAdapter roomAdapter;
    Context context;
    CommunicationInterface listener;
    ArrayList<Room> roomArrayList;

    ACProgressPie dialog;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CommunicationInterface) {
            listener = (CommunicationInterface) context;
        } else {
            throw new RuntimeException(context.toString() + "Can phai implement");
        }
    }

    public RoomFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_room, container, false);

        initialize(view);

        getDataRoom(SharedPreferencesUtils.loadToken(getContext()));

        rclRoomList.addOnItemTouchListener(
                new RecyclerItemClickListener(context, rclRoomList, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        listener.sendData(roomArrayList.get(position).getId(), roomArrayList.get(position).getName());
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        Toast.makeText(getContext(), "Long Click", Toast.LENGTH_SHORT).show();
                        PopupMenu popupMenu = new PopupMenu(getContext(),rclRoomList.getChildAt(position));
                        popupMenu.getMenuInflater().inflate(R.menu.room_menu,popupMenu.getMenu());
                        popupMenu.setOnMenuItemClickListener(menuItem -> {
                            switch (menuItem.getItemId()){
                                case R.id.menu_delete:{
                                    deleteRoom(SharedPreferencesUtils.loadToken(getContext()),roomArrayList.get(position).getId());
                                    break;
                                }
                            }
                            return false;
                        });
                        popupMenu.show();
                    }
                })
        );

        return view;
    }

    public void initialize(View view) {

        rclRoomList = view.findViewById(R.id.rclRoom);
        btnAdd = view.findViewById(R.id.btn_add_room);
        tvNoRoom = view.findViewById(R.id.tvNoRoom);

        roomArrayList=new ArrayList<>();
        roomAdapter=new RoomAdapter(new ArrayList<Room>(),getContext());

        rclRoomList.setAdapter(roomAdapter);
        rclRoomList.setLayoutManager(new LinearLayoutManager(getContext()));

        btnAdd.setOnClickListener(this);

        dialog = new ACProgressPie.Builder(getContext())
                .ringColor(Color.WHITE)
                .pieColor(Color.WHITE)
                .updateType(ACProgressConstant.PIE_AUTO_UPDATE)
                .build();
        dialog.setCancelable(true);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_room:
                showAddRoomFragment();
                break;
        }
    }

    public void deleteRoom(String token, String roomId){
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiRetrofit.URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        roomUtils = retrofit.create(RoomUtils.class);

        Call<DeleteRespones> call = roomUtils.deleteRoom(token, roomId);
        call.enqueue(new Callback<DeleteRespones>() {
            @Override
            public void onResponse(Call<DeleteRespones> call, Response<DeleteRespones> response) {
                DeleteRespones deleteRespones;
                deleteRespones = response.body();
                if (deleteRespones.isSuccess()){
                    Toast.makeText(getContext(), deleteRespones.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), deleteRespones.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteRespones> call, Throwable t) {
                Toast.makeText(getContext(), "Can't load data, please try again!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showAddRoomFragment() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment = new AddRoomFragment();
        transaction.replace(R.id.frame_container, fragment).addToBackStack("addRoomFragment");
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        transaction.commit();
    }

    public void getDataRoom(String token) {
        dialog.show();
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiRetrofit.URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        roomUtils = retrofit.create(RoomUtils.class);

        Call<ArrayList<Room>> call = roomUtils.getRoom(token);
        call.enqueue(new Callback<ArrayList<Room>>() {
            @Override
            public void onResponse(Call<ArrayList<Room>> call, Response<ArrayList<Room>> response) {
                ArrayList<Room> rooms=new ArrayList<>();
                rooms = response.body();

                if (rooms!=null){
                    dialog.dismiss();
                    roomArrayList=rooms;
                    roomAdapter.updateList(rooms);
                    roomAdapter.notifyDataSetChanged();
                    if (rooms.size()<1){
                        tvNoRoom.setVisibility(View.VISIBLE);
                    }else {
                        tvNoRoom.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Room>> call, Throwable t) {
                dialog.dismiss();
                CustomToast.makeText(getContext(),"Can't load data, please try again!",CustomToast.LENGTH_LONG,CustomToast.ERROR,false).show();
            }
        });
    }

}
