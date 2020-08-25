package com.example.chikaapp.fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chikaapp.R;
import com.example.chikaapp.SharedPreferencesUtils;
import com.example.chikaapp.action.CustomToast;
import com.example.chikaapp.action.RecyclerItemClickListener;
import com.example.chikaapp.adapter.RoomAdapter;
import com.example.chikaapp.api.ApiRetrofit;
import com.example.chikaapp.api.RoomUtils;
import com.example.chikaapp.model.DeleteResponse;
import com.example.chikaapp.model.Room;

import com.example.chikaapp.model.User;
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
    RecyclerView rclRoomList, rclDeviceList;

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
                        listener.RoomToDevices(roomArrayList.get(position).getId(), roomArrayList.get(position).getName());
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        PopupMenu popupMenu = new PopupMenu(getContext(),rclRoomList.getChildAt(position));
                        popupMenu.getMenuInflater().inflate(R.menu.room_menu,popupMenu.getMenu());
                        popupMenu.setOnMenuItemClickListener(menuItem -> {
                            switch (menuItem.getItemId()){
                                case R.id.menu_delete:{
                                    User user=SharedPreferencesUtils.loadSelf(getContext());
                                    if (user.getRole().equals("HOME_MASTER")||user.getRole().equals("ADMIN")){
                                        deleteRoom(SharedPreferencesUtils.loadToken(getContext()),roomArrayList.get(position).getId());
                                        reloadFragment();
                                    } else {
                                        CustomToast.makeText(getContext(),"You're not allow to delete!",CustomToast.LENGTH_LONG,CustomToast.WARNING,false);
                                    }
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

    private void reloadFragment() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment = new RoomFragment();
        transaction.replace(R.id.frame_container, fragment).addToBackStack("roomFragment");
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        transaction.commit();
    }

    public void initialize(View view) {

        btnAdd = view.findViewById(R.id.btn_add_room);
        tvNoRoom = view.findViewById(R.id.tvNoRoom);

        //Adapter
        roomArrayList=new ArrayList<>();
        roomAdapter=new RoomAdapter(new ArrayList<Room>(),getContext());
        //RecyclerView
        rclRoomList = view.findViewById(R.id.rclRoom);
        rclRoomList.setAdapter(roomAdapter);
        RecyclerView.LayoutManager mLayoutManager
                = new GridLayoutManager(getContext(),1);
        rclRoomList.setLayoutManager(mLayoutManager);

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

        Call<DeleteResponse> call = roomUtils.deleteRoom(token, roomId);
        call.enqueue(new Callback<DeleteResponse>() {
            @Override
            public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response) {
                DeleteResponse deleteResponse;
                deleteResponse = response.body();
                if (deleteResponse.isSuccess()){
                    CustomToast.makeText(getContext(), deleteResponse.getMessage(), CustomToast.LENGTH_SHORT,CustomToast.SUCCESS,false).show();
                } else {
                    CustomToast.makeText(getContext(), deleteResponse.getMessage(), CustomToast.LENGTH_SHORT,CustomToast.WARNING,false).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteResponse> call, Throwable t) {
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
