package com.example.androidmedical.api;

import com.example.androidmedical.model.LoginRequest;
import com.example.androidmedical.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("api/Auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);
}
