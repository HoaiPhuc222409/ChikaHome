package com.example.chikaapp.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.chikaapp.R;
import com.example.chikaapp.SharedPreferencesUtils;
import com.example.chikaapp.activity.LoginActivity;
import com.example.chikaapp.api.APIAccount;
import com.example.chikaapp.api.ApiRetrofit;
import com.example.chikaapp.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment implements View.OnClickListener{

    Button btnSignOut;
    TextView tvName, tvUserName, tvEmail;


    public UserFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user, container, false);

        loadUser();

        initailize(view);

        User user=SharedPreferencesUtils.loadSelf(getContext());


        tvUserName.setText(user.getUsername());
        tvName.setText(user.getName());
        tvEmail.setText(user.getEmail());



        return view;

    }

    public void initailize(View view){
        btnSignOut = view.findViewById(R.id.btnLogOut);
        tvUserName=view.findViewById(R.id.tv_username);
        tvName=view.findViewById(R.id.tv_name);
        tvEmail=view.findViewById(R.id.tv_email);

        btnSignOut.setOnClickListener(this);
    }

    private void loadUser(){
        String token=SharedPreferencesUtils.loadToken(getContext());
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(ApiRetrofit.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIAccount iAccount=retrofit.create(APIAccount.class);
        Call<User> userCall=iAccount.getUser(token);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user=response.body();
                if (user!=null){
                    SharedPreferencesUtils.saveSelf(getContext(),user);
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
        }
    }
}
