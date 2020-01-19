package com.example.chikaapp.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chikaapp.R;
import com.example.chikaapp.SharedPreferencesUtils;
import com.example.chikaapp.adapter.RoomAdapter;
import com.example.chikaapp.api.ApiRetrofit;
import com.example.chikaapp.api.RoomUtils;
import com.example.chikaapp.model.Room;
import com.example.chikaapp.request.CreateRoomRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoomFragment extends Fragment implements View.OnClickListener {

    private Button btnAdd;
    private RecyclerView rclRoomList;
    private RoomAdapter roomAdapter;

    RoomUtils roomUtils;

    public RoomFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_room, container, false);
        initialize(view);

        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiRetrofit.URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        roomUtils = retrofit.create(RoomUtils.class);
        Call<ArrayList<Room>> call = roomUtils.getRoom(SharedPreferencesUtils.loadToken(getContext()));
        call.enqueue(new Callback<ArrayList<Room>>() {
            @Override
            public void onResponse(Call<ArrayList<Room>> call, Response<ArrayList<Room>> response) {
                ArrayList<Room> rooms=new ArrayList<>();
                rooms = response.body();
                if (rooms != null) {

                    Toast.makeText(getContext(), "Get Success", Toast.LENGTH_SHORT).show();
                    roomAdapter.updateList(rooms);
                    roomAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(getContext(), "Get Fail", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Room>> call, Throwable t) {

            }

        });

        return view;
    }

    public void initialize(View view) {
        rclRoomList = view.findViewById(R.id.rclRoom);
        btnAdd = view.findViewById(R.id.btn_add_room);

        roomAdapter=new RoomAdapter(new ArrayList<Room>(),getContext());
        rclRoomList.setAdapter(roomAdapter);
        rclRoomList.setLayoutManager(new LinearLayoutManager(getContext()));

        btnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_room:
                showAddRoomFragment();
        }
    }

    public void showAddRoomFragment() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment = new AddRoomFragment();
        transaction.replace(R.id.frame_container, fragment).addToBackStack("addRoomFragment");
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        transaction.commit();
    }

}
