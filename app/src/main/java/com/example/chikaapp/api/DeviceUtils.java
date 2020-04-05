package com.example.chikaapp.api;

import com.example.chikaapp.model.Devices;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface DeviceUtils {
    @Headers(
            {
                    "Content-Type:application/json",
            }
    )

    @GET("/device/room_id/{id}")
    Call<ArrayList<Devices>> getDevices(@Header ("Authorization") String token, @Path("id") String id);

}
