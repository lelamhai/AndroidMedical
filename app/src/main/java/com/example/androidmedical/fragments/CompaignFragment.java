package com.example.androidmedical.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.androidmedical.adapters.CampaignAdapter;
import com.example.androidmedical.api.RetrofitClient;
import com.example.androidmedical.model.CampaignItem;
import com.example.androidmedical.model.CampaignListData;
import com.example.androidmedical.model.CampaignListResponse;
import com.example.androidmedical.model.CampaignSearchResponse;
import com.example.androidmedical.screens.MapScreen;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompaignFragment extends Fragment {

    private RecyclerView rvCampaigns;
    private ProgressBar progressBar;
    private TextInputEditText etSearch;

    private CampaignAdapter campaignAdapter;
    private List<CampaignItem> campaignItemList;

    public CompaignFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compaign, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvCampaigns = view.findViewById(R.id.rvCampaigns);
        progressBar = view.findViewById(R.id.progressBar);
        etSearch = view.findViewById(R.id.etSearch);

        rvCampaigns.setLayoutManager(new LinearLayoutManager(getContext()));
        campaignItemList = new ArrayList<>();

        campaignAdapter = new CampaignAdapter(campaignItemList, new CampaignAdapter.OnCampaignClickListener() {
            @Override
            public void onRegisterClick(CampaignItem campaign) {
                Toast.makeText(getContext(), "Đăng ký thành công chiến dịch: " + campaign.getTitle(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDirectionsClick(CampaignItem campaign) {
                Intent intent = new Intent(getContext(), MapScreen.class);
                intent.putExtra("campaign_title", campaign.getTitle());
                intent.putExtra("campaign_location", campaign.getLocation());
                startActivity(intent);
            }
        });

        rvCampaigns.setAdapter(campaignAdapter);

        // Search listener calling API
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s != null ? s.toString().trim() : "";
                if (query.isEmpty()) {
                    fetchCampaigns();
                } else {
                    searchCampaigns(query);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        fetchCampaigns();
    }

    private void fetchCampaigns() {
        if (getContext() == null) return;
        progressBar.setVisibility(View.VISIBLE);

        RetrofitClient.getApiService().getCampaignList().enqueue(new Callback<CampaignListResponse>() {
            @Override
            public void onResponse(@NonNull Call<CampaignListResponse> call, @NonNull Response<CampaignListResponse> response) {
                if (!isAdded()) return;
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    CampaignListResponse listResponse = response.body();
                    CampaignListData data = listResponse.getData();
                    if (data != null && data.getItems() != null) {
                        campaignAdapter.setCampaignList(data.getItems());
                    } else {
                        campaignAdapter.setCampaignList(new ArrayList<>());
                        Toast.makeText(getContext(), "Không có dữ liệu chiến dịch", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Không thể tải danh sách chiến dịch", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CampaignListResponse> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void searchCampaigns(String query) {
        if (getContext() == null) return;
        progressBar.setVisibility(View.VISIBLE);

        RetrofitClient.getApiService().searchCampaigns(query).enqueue(new Callback<CampaignSearchResponse>() {
            @Override
            public void onResponse(@NonNull Call<CampaignSearchResponse> call, @NonNull Response<CampaignSearchResponse> response) {
                if (!isAdded()) return;
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    CampaignSearchResponse searchResponse = response.body();
                    List<CampaignItem> searchResults = searchResponse.getData();
                    if (searchResults != null) {
                        campaignAdapter.setCampaignList(searchResults);
                    } else {
                        campaignAdapter.setCampaignList(new ArrayList<>());
                    }
                } else {
                    Toast.makeText(getContext(), "Không tìm thấy chiến dịch phù hợp", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CampaignSearchResponse> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
