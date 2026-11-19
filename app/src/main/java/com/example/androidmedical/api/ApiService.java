package com.example.androidmedical.api;

import com.example.androidmedical.model.LoginRequest;
import com.example.androidmedical.model.LoginResponse;
import com.example.androidmedical.model.RegisterRequest;
import com.example.androidmedical.model.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("api/Auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("api/Auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);
}
