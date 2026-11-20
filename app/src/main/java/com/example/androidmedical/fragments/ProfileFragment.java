package com.example.androidmedical.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidmedical.R;
import com.example.androidmedical.api.RetrofitClient;
import com.example.androidmedical.model.UserProfile;
import com.example.androidmedical.model.UserProfileResponse;
import com.example.androidmedical.screens.LoginScreen;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private TextView tvFullName, tvRole, tvBloodType, tvEmail, tvPhone, tvGender, tvDob, tvCreatedAt;
    private ProgressBar progressBar;
    private MaterialButton btnLogout;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvFullName = view.findViewById(R.id.tvFullName);
        tvRole = view.findViewById(R.id.tvRole);
        tvBloodType = view.findViewById(R.id.tvBloodType);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvPhone = view.findViewById(R.id.tvPhone);
        tvGender = view.findViewById(R.id.tvGender);
        tvDob = view.findViewById(R.id.tvDob);
        tvCreatedAt = view.findViewById(R.id.tvCreatedAt);
        progressBar = view.findViewById(R.id.progressBar);
        btnLogout = view.findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(v -> {
            if (getContext() != null) {
                SharedPreferences prefs = getContext().getSharedPreferences("MedicalPrefs", Context.MODE_PRIVATE);
                prefs.edit().clear().apply();
                Intent intent = new Intent(getContext(), LoginScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        fetchUserProfile();
    }

    private void fetchUserProfile() {
        if (getContext() == null) return;
        progressBar.setVisibility(View.VISIBLE);

        SharedPreferences prefs = getContext().getSharedPreferences("MedicalPrefs", Context.MODE_PRIVATE);
        String token = prefs.getString("ACCESS_TOKEN", "");
        String authHeader = "Bearer " + token;

        RetrofitClient.getApiService().getUserProfile(authHeader).enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserProfileResponse> call, @NonNull Response<UserProfileResponse> response) {
                if (!isAdded()) return;
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    UserProfileResponse res = response.body();
                    UserProfile profile = res.getData();
                    if (profile != null) {
                        String fullName = (profile.getFirstName() != null ? profile.getFirstName() : "") +
                                " " + (profile.getLastName() != null ? profile.getLastName() : "");
                        tvFullName.setText(fullName.trim().isEmpty() ? "Chưa cập nhật" : fullName.trim());
                        tvRole.setText(profile.getRole() != null ? profile.getRole() : "User");
                        tvBloodType.setText(profile.getBloodType() != null && !profile.getBloodType().isEmpty() ? profile.getBloodType() : "Chưa rõ");
                        tvEmail.setText(profile.getEmail() != null ? profile.getEmail() : "Chưa có");
                        tvPhone.setText(profile.getPhone() != null && !profile.getPhone().isEmpty() ? profile.getPhone() : "Chưa cập nhật");
                        tvGender.setText(profile.getGender() != null && !profile.getGender().isEmpty() ? profile.getGender() : "Chưa cập nhật");

                        String dob = profile.getDateOfBirth();
                        tvDob.setText(dob != null && !dob.isEmpty() ? dob.replace("T", " ") : "Chưa cập nhật");

                        String createdAt = profile.getCreatedAt();
                        tvCreatedAt.setText(createdAt != null && !createdAt.isEmpty() ? createdAt.replace("T", " ") : "Chưa cập nhật");
                    }
                } else {
                    Toast.makeText(getContext(), "Không thể tải thông tin cá nhân", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserProfileResponse> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
