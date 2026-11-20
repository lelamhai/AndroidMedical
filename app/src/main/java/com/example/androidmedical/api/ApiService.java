package com.example.androidmedical.api;

import com.example.androidmedical.model.CampaignListResponse;
import com.example.androidmedical.model.CampaignRegistrationResponse;
import com.example.androidmedical.model.CampaignSearchResponse;
import com.example.androidmedical.model.LoginRequest;
import com.example.androidmedical.model.LoginResponse;
import com.example.androidmedical.model.RegisterRequest;
import com.example.androidmedical.model.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @POST("api/Auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("api/Auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);

    @GET("api/Campaign/list")
    Call<CampaignListResponse> getCampaignList();

    @GET("api/Campaign/search")
    Call<CampaignSearchResponse> searchCampaigns(@Query("text") String text);

    @GET("api/CampaignRegistration/future")
    Call<CampaignRegistrationResponse> getFutureRegistrations(@Header("Authorization") String authorization);

    @GET("api/CampaignRegistration/history")
    Call<CampaignRegistrationResponse> getHistoryRegistrations(@Header("Authorization") String authorization);
}
