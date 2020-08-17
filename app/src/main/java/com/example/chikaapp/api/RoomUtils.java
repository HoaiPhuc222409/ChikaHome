package com.example.chikaapp.api;

import com.example.chikaapp.model.DeleteResponse;
import com.example.chikaapp.model.Room;
import com.example.chikaapp.request.CreateRoomRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RoomUtils {

    @Headers(
            {
                    "Content-Type:application/json",
            }
    )
    //create room
    @POST("/room")
    Call<Room> createRoom(@Header ("Authorization") String token
            ,@Body CreateRoomRequest createRoomRequest);

    //get all room
    @GET("/room")
    Call<ArrayList<Room>> getRoom(@Header("Authorization") String token);

    //delete room by id
    @DELETE("/room/{id}")
    Call<DeleteResponse> deleteRoom(@Header("Authorization") String token
            , @Path("id") String id);

}
