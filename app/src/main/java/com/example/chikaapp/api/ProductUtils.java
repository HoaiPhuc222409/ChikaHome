package com.example.chikaapp.api;

import com.example.chikaapp.model.Devices;
import com.example.chikaapp.model.Products;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface ProductUtils {
    @Headers(
            {
                    "Content-Type:application/json",
            }
    )

    @GET("/product")
    Call<ArrayList<Products>> getProducts(@Header("Authorization") String token);

    @GET("/device/topic/{tp}")
    Call<ArrayList> getUsedButton(@Header("Authorization") String token, @Path("tp") String topic);
}
