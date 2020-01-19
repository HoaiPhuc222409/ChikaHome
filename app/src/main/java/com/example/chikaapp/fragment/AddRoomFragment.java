package com.example.chikaapp.fragment;


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

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddRoomFragment extends Fragment {

    CircleImageView img_Add;
    ImageView img_Room;
    EditText edt_Room_Name;
    TextView tvDone;

    RoomUtils roomUtils;

    public int room_url_image;
    public String room_name;

    Image logo=new Image();

    public AddRoomFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        View view = inflater.inflate(R.layout.fragment_add_room, container, false);
        img_Add = view.findViewById(R.id.img_add_icon);
        img_Room=view.findViewById(R.id.img_icon_room);
        edt_Room_Name=view.findViewById(R.id.edt_room_name);
        tvDone=view.findViewById(R.id.tvDone);

        tvDone.setOnClickListener(v->{

            if (!edt_Room_Name.getText().toString().equals("")){
                room_name=edt_Room_Name.getText().toString();

                Gson gson = new GsonBuilder().setLenient().create();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(ApiRetrofit.URL)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();
                roomUtils = retrofit.create(RoomUtils.class);
                Call<Room> call = roomUtils.createRoom(SharedPreferencesUtils.loadToken(getContext()),new CreateRoomRequest(logo.getId(),room_name));
                call.enqueue(new Callback<Room>() {
                    @Override
                    public void onResponse(Call<Room> call, Response<Room> response) {
                        Room room = response.body();
                        if (room != null) {

                            Toast.makeText(getContext(), "Create Success", Toast.LENGTH_SHORT).show();
                            FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();

                            fragmentTransaction.replace(R.id.frame_container,new RoomFragment());
                            fragmentTransaction.commit();

                        } else {
                            Toast.makeText(getContext(), "Create Fail", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Room> call, Throwable t) {

                    }


                });

            } else {
                Toast.makeText(getContext(), "Please input your room's name", Toast.LENGTH_SHORT).show();
            }


        });

        List<Image> image_details = getListData();

        final GridView gridView = view.findViewById(R.id.gridImage);
        gridView.setAdapter(new ImageAdapter(getContext(), image_details));

        img_Add.setOnClickListener(v -> {
            gridView.setVisibility(View.VISIBLE);
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = gridView.getItemAtPosition(position);
                Image image = (Image) o;
                logo=image;
                img_Room.setImageResource(image.getUrl_image());
                room_url_image=image.getUrl_image();
            }
        });


        final GestureDetector gesture = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onDown(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                           float velocityY) {
                        Log.i("tag", "onFling has been called!");
                        final int SWIPE_MIN_DISTANCE = 120;
                        final int SWIPE_MAX_OFF_PATH = 250;
                        final int SWIPE_THRESHOLD_VELOCITY = 200;
                        try {
                            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                                return false;
                            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

                            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction transaction = fragmentManager.beginTransaction().setCustomAnimations(R.anim.fade_out, R.anim.fade_in);
                                fragmentManager.popBackStack();
                                transaction.commit();
                            }
                        } catch (Exception e) {

                        }
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });
        return view;
    }

    private List<Image> getListData() {
        List<Image> list = new ArrayList<>();

        list.add(new Image(0, R.drawable.bg_button, "livingRoom"));
        list.add(new Image(1, R.drawable.ic_camera, "bedRoom"));
        list.add(new Image(2, R.drawable.bg_item, "garden"));
        list.add(new Image(3, R.drawable.bg_main, "kitchen"));

        return list;
    }

}
