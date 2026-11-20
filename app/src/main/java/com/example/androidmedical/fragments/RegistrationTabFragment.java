package com.example.androidmedical.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidmedical.R;
import com.example.androidmedical.adapters.RegistrationAdapter;
import com.example.androidmedical.api.RetrofitClient;
import com.example.androidmedical.model.CampaignItem;
import com.example.androidmedical.model.CampaignRegistrationResponse;
import com.example.androidmedical.screens.MapScreen;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationTabFragment extends Fragment {

    private static final String ARG_TYPE = "tab_type";
    private String tabType; // "future" or "history"

    private RecyclerView rvCampaigns;
    private ProgressBar progressBar;
    private RegistrationAdapter adapter;
    private List<CampaignItem> itemList;

    public static RegistrationTabFragment newInstance(String type) {
        RegistrationTabFragment fragment = new RegistrationTabFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tabType = getArguments().getString(ARG_TYPE, "future");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registration_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvCampaigns = view.findViewById(R.id.rvCampaigns);
        progressBar = view.findViewById(R.id.progressBar);

        rvCampaigns.setLayoutManager(new LinearLayoutManager(getContext()));
        itemList = new ArrayList<>();

        boolean isFuture = "future".equals(tabType);

        adapter = new RegistrationAdapter(itemList, isFuture, new RegistrationAdapter.OnRegistrationClickListener() {
            @Override
            public void onActionClick(CampaignItem campaign) {
                if (isFuture) {
                    Toast.makeText(getContext(), "Hủy đăng ký chiến dịch: " + campaign.getTitle(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onDirectionsClick(CampaignItem campaign) {
                Intent intent = new Intent(getContext(), MapScreen.class);
                intent.putExtra("campaign_title", campaign.getTitle());
                intent.putExtra("campaign_location", campaign.getLocation());
                startActivity(intent);
            }
        });

        rvCampaigns.setAdapter(adapter);
        fetchData();
    }

    private void fetchData() {
        if (getContext() == null) return;
        progressBar.setVisibility(View.VISIBLE);

        SharedPreferences prefs = getContext().getSharedPreferences("MedicalPrefs", Context.MODE_PRIVATE);
        String token = prefs.getString("ACCESS_TOKEN", "");
        String authHeader = "Bearer " + token;

        boolean isFuture = "future".equals(tabType);
        Call<CampaignRegistrationResponse> call = isFuture
                ? RetrofitClient.getApiService().getFutureRegistrations(authHeader)
                : RetrofitClient.getApiService().getHistoryRegistrations(authHeader);

        call.enqueue(new Callback<CampaignRegistrationResponse>() {
            @Override
            public void onResponse(@NonNull Call<CampaignRegistrationResponse> call, @NonNull Response<CampaignRegistrationResponse> response) {
                if (!isAdded()) return;
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    CampaignRegistrationResponse res = response.body();
                    List<CampaignItem> data = res.getData();
                    if (data != null) {
                        adapter.setCampaignList(data);
                    } else {
                        adapter.setCampaignList(new ArrayList<>());
                    }
                } else {
                    String errorMsg = "Không thể tải danh sách đăng ký";
                    if (response.code() == 401) {
                        errorMsg = "Phiên đăng nhập hết hạn. Vui lòng đăng nhập lại.";
                    }
                    Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
                    adapter.setCampaignList(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(@NonNull Call<CampaignRegistrationResponse> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
