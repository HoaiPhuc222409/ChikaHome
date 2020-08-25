package com.example.chikaapp.api;

import com.example.chikaapp.model.DeleteResponse;
import com.example.chikaapp.model.Devices;
import com.example.chikaapp.model.ScriptDevices;
import com.example.chikaapp.request.CreateDeviceRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DeviceUtils {
    @Headers(
            {
                    "Content-Type:application/json",
            }
    )

    @GET("/device/room_id/{id}")
    Call<ArrayList<Devices>> getDevices(@Header ("Authorization") String token
            , @Path("id") String id);

    @GET("/device/script")
    Call<ArrayList<ScriptDevices>> getAllDevice(@Header("Authorization") String token);

    @POST("/device")
    Call<Devices> createDevice(@Header ("Authorization") String token
            , @Body CreateDeviceRequest request);

    @DELETE("/device/{id}")
    Call<DeleteResponse> deleteDevices(@Header ("Authorization") String token
            , @Path("id") String id);
}
