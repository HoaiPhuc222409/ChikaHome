package com.example.chikaapp.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.chikaapp.R;
import com.example.chikaapp.SharedPreferencesUtils;
import com.example.chikaapp.activity.LoginActivity;
import com.example.chikaapp.api.UserUtils;
import com.example.chikaapp.api.ApiRetrofit;
import com.example.chikaapp.model.User;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment implements View.OnClickListener{

    Button btnSignOut, btnAddHomeUser, btnSupport, btnEditInfo;
    TextView tvName, tvPhone, tvEmail;
    ImageView avatar;
    CommunicationInterface listener;


    public UserFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user, container, false);

        initialize(view);

        loadUser(getContext());



        return view;
    }



    public void initialize(View view){
        btnSignOut = view.findViewById(R.id.btnLogOut);
        btnEditInfo = view.findViewById(R.id.btn_edit_user_info);
        btnSupport = view.findViewById(R.id.btn_support);
        btnAddHomeUser=view.findViewById(R.id.btn_add_HomeUser);
        tvPhone =view.findViewById(R.id.tv_phone);
        tvName=view.findViewById(R.id.tv_username);
        tvEmail=view.findViewById(R.id.tv_email);
        avatar=view.findViewById(R.id.img_profile);

        btnSignOut.setOnClickListener(this);
        btnAddHomeUser.setOnClickListener(this);
        btnEditInfo.setOnClickListener(this);
        btnSupport.setOnClickListener(this);
    }


    public void userInfo(Context context){
        User user=SharedPreferencesUtils.loadSelf(context);
        if (user!=null){
            tvPhone.setText(user.getPhone());
            tvName.setText(user.getName());
            tvEmail.setText(user.getEmail());
            Picasso.get().load(user.getAvatar()).into(avatar);
        }


    }

    private void loadUser(Context context){
        String token=SharedPreferencesUtils.loadToken(context);
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(ApiRetrofit.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserUtils iAccount=retrofit.create(UserUtils.class);
        Call<User> userCall=iAccount.getUser(token);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user=response.body();
                if (user!=null){
                    SharedPreferencesUtils.saveSelf(context,user);
                    tvPhone.setText(user.getPhone());
                    tvName.setText(user.getName());
                    tvEmail.setText(user.getEmail());

                    Picasso.get().load(user.getAvatar()).into(avatar);
                    if (user.getRole().equals("HOME_MASTER")||user.getRole().equals("ADMIN")){
                        btnAddHomeUser.setVisibility(View.VISIBLE);
                    } else{
                        btnAddHomeUser.setVisibility(View.GONE);
                    }
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                return;
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnLogOut:{
                SharedPreferencesUtils.clearToken(getContext());
                startActivity(new Intent(getContext(), LoginActivity.class));
                break;
            }
            case R.id.btn_add_HomeUser:{
                Toast.makeText(getContext(), "Add_Home_User", Toast.LENGTH_SHORT).show();
                break;
            }

            case R.id.btn_edit_user_info:
                showEditInfoFragment();
                break;
//
//            case R.id.btn_support:
        }
    }

    public void showEditInfoFragment(){
        FragmentManager manager=getFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.addToBackStack(null);
        EditUserInfoFragment editUserInfoFragment = new EditUserInfoFragment();
        transaction.replace(R.id.frame_container,editUserInfoFragment);
        transaction.commit();
    }

}
