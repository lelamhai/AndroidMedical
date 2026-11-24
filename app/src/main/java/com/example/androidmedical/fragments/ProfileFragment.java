package com.example.androidmedical.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.example.androidmedical.model.MedicalInfoData;
import com.example.androidmedical.model.UserInfo;
import com.example.androidmedical.model.UserMedicalInfoResponse;
import com.example.androidmedical.screens.LoginScreen;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private TextView tvFullName, tvBloodType, tvDiseaseNotes, tvResultDate, tvEmail, tvPhone, tvGender, tvDob, tvCreatedAt;
    private ProgressBar progressBar;
    private MaterialButton btnLogout;
    private ShapeableImageView ivAvatar;
    private View cardImageIcon;
    private ActivityResultLauncher<String> imagePickerLauncher;

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
        tvBloodType = view.findViewById(R.id.tvBloodType);
        tvDiseaseNotes = view.findViewById(R.id.tvDiseaseNotes);
        tvResultDate = view.findViewById(R.id.tvResultDate);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvPhone = view.findViewById(R.id.tvPhone);
        tvGender = view.findViewById(R.id.tvGender);
        tvDob = view.findViewById(R.id.tvDob);
        tvCreatedAt = view.findViewById(R.id.tvCreatedAt);
        progressBar = view.findViewById(R.id.progressBar);
        btnLogout = view.findViewById(R.id.btnLogout);
        ivAvatar = view.findViewById(R.id.ivAvatar);
        cardImageIcon = view.findViewById(R.id.cardImageIcon);

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                ivAvatar.setImageURI(uri);
                Toast.makeText(getContext(), "Đã cập nhật ảnh đại diện", Toast.LENGTH_SHORT).show();
            }
        });

        View.OnClickListener pickImageListener = v -> imagePickerLauncher.launch("image/*");
        ivAvatar.setOnClickListener(pickImageListener);
        cardImageIcon.setOnClickListener(pickImageListener);

        btnLogout.setOnClickListener(v -> {
            if (getContext() != null) {
                SharedPreferences prefs = getContext().getSharedPreferences("MedicalPrefs", Context.MODE_PRIVATE);
                prefs.edit().clear().apply();
                Intent intent = new Intent(getContext(), LoginScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        fetchUserMedicalInfo();
    }

    private void fetchUserMedicalInfo() {
        if (getContext() == null) return;
        progressBar.setVisibility(View.VISIBLE);

        SharedPreferences prefs = getContext().getSharedPreferences("MedicalPrefs", Context.MODE_PRIVATE);
        String token = prefs.getString("ACCESS_TOKEN", "");
        String authHeader = "Bearer " + token;

        RetrofitClient.getApiService().getUserMedicalInfo(authHeader).enqueue(new Callback<UserMedicalInfoResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserMedicalInfoResponse> call, @NonNull Response<UserMedicalInfoResponse> response) {
                if (!isAdded()) return;
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    UserMedicalInfoResponse res = response.body();
                    MedicalInfoData medicalData = res.getData();
                    if (medicalData != null) {
                        // Blood type, disease notes, result date
                        tvBloodType.setText(medicalData.getBloodType() != null && !medicalData.getBloodType().isEmpty() ? medicalData.getBloodType() : "Chưa rõ");
                        tvDiseaseNotes.setText(medicalData.getDiseaseNotes() != null && !medicalData.getDiseaseNotes().isEmpty() ? medicalData.getDiseaseNotes() : "Không có");
                        
                        String resultDate = medicalData.getResultDate();
                        tvResultDate.setText(resultDate != null && !resultDate.isEmpty() ? resultDate.replace("T", " ") : "Chưa cập nhật");

                        // User info
                        UserInfo user = medicalData.getUser();
                        if (user != null) {
                            String fullName = user.getFullName() != null && !user.getFullName().isEmpty() 
                                    ? user.getFullName() 
                                    : ((user.getFirstName() != null ? user.getFirstName() : "") + " " + (user.getLastName() != null ? user.getLastName() : ""));
                            tvFullName.setText(fullName.trim().isEmpty() ? "Chưa cập nhật" : fullName.trim());
                            tvEmail.setText(user.getEmail() != null ? user.getEmail() : "Chưa có");
                            tvPhone.setText(user.getPhone() != null && !user.getPhone().isEmpty() ? user.getPhone() : "Chưa cập nhật");
                            tvGender.setText(user.getGender() != null && !user.getGender().isEmpty() ? user.getGender() : "Chưa cập nhật");

                            String dob = user.getDateOfBirth();
                            tvDob.setText(dob != null && !dob.isEmpty() ? dob.replace("T", " ") : "Chưa cập nhật");

                            String createdAt = user.getCreatedAt();
                            tvCreatedAt.setText(createdAt != null && !createdAt.isEmpty() ? createdAt.replace("T", " ") : "Chưa cập nhật");
                        }
                    }
                } else {
                    String errorMsg = "Không thể tải thông tin y tế cá nhân";
                    if (response.code() == 401) {
                        errorMsg = "Phiên đăng nhập hết hạn. Vui lòng đăng nhập lại.";
                    }
                    Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserMedicalInfoResponse> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
