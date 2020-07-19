package com.example.chikaapp.api;

import com.example.chikaapp.model.User;
import com.example.chikaapp.request.EditInfoUserRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PUT;

public interface UserUtils {

    @Headers({
            "Content-Type:application/json"
    })

    @GET("user/info")
    Call<User> getUser (@Header("Authorization") String token);

    @PUT("user/user_info")
    Call<User> editUser (@Header("Authorization") String token, @Body EditInfoUserRequest request);
    
}
