package com.ahn.vehiclerentapp.network;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

public interface ApiService {

    @POST("send")
    Call<String> sendMassage(
            @HeaderMap HashMap<String, String> headers,
            @Body String massageBody
    );
}
