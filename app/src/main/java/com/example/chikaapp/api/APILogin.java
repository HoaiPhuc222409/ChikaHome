package com.example.chikaapp.api;

import com.example.chikaapp.model.Token;
import com.example.chikaapp.request.LoginRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APILogin {

    @Headers({
            "Content-Type:application/json",
    })

    @POST("auth/signin")
    Call<Token> loginAccount(@Body LoginRequest loginRequest);

}
