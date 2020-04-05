package com.example.chikaapp.api;

import com.example.chikaapp.model.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

public interface APIAccount {

    @Headers({
            "Content-Type:application/json"
    })
    @GET("user/info")
    Call<User> getUser (@Header("Authorization") String token);
}
