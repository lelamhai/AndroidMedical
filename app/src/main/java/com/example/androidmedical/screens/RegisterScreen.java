package com.example.androidmedical.screens;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidmedical.R;
import com.example.androidmedical.api.RetrofitClient;
import com.example.androidmedical.model.RegisterRequest;
import com.example.androidmedical.model.RegisterResponse;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterScreen extends AppCompatActivity {

    private TextInputEditText etFirstName, etLastName, etEmail, etPassword;
    private RadioGroup rgGender;
    private MaterialButton btnRegisterSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etRegisterEmail);
        etPassword = findViewById(R.id.etRegisterPassword);
        rgGender = findViewById(R.id.rgGender);
        btnRegisterSubmit = findViewById(R.id.btnRegisterSubmit);

        btnRegisterSubmit.setOnClickListener(v -> performRegister());

        findViewById(R.id.tvBackToLogin).setOnClickListener(v -> finish());
    }

    private void performRegister() {
        String firstName = etFirstName.getText() != null ? etFirstName.getText().toString().trim() : "";
        String lastName = etLastName.getText() != null ? etLastName.getText().toString().trim() : "";
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";

        if (firstName.isEmpty()) {
            etFirstName.setError("Vui lòng nhập họ");
            return;
        }
        if (lastName.isEmpty()) {
            etLastName.setError("Vui lòng nhập tên");
            return;
        }
        if (email.isEmpty()) {
            etEmail.setError("Vui lòng nhập email");
            return;
        }
        if (password.isEmpty()) {
            etPassword.setError("Vui lòng nhập mật khẩu");
            return;
        }

        String gender = "Nam";
        int selectedId = rgGender.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton rbSelected = findViewById(selectedId);
            if (rbSelected != null) {
                gender = rbSelected.getText().toString();
            }
        }

        btnRegisterSubmit.setEnabled(false);
        btnRegisterSubmit.setText("Đang đăng ký...");

        RegisterRequest request = new RegisterRequest(email, password, firstName, lastName, gender);
        RetrofitClient.getApiService().register(request).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                btnRegisterSubmit.setEnabled(true);
                btnRegisterSubmit.setText("ĐĂNG KÝ");

                if (response.isSuccessful() && response.body() != null) {
                    RegisterResponse regResponse = response.body();
                    String msg = regResponse.getMessage() != null ? regResponse.getMessage() : "User registered successfully.";
                    Toast.makeText(RegisterScreen.this, msg, Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    String errorMsg = "Đăng ký thất bại. Email có thể đã tồn tại!";
                    if (response.errorBody() != null) {
                        try {
                            errorMsg = response.errorBody().string();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(RegisterScreen.this, errorMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                btnRegisterSubmit.setEnabled(true);
                btnRegisterSubmit.setText("ĐĂNG KÝ");
                Toast.makeText(RegisterScreen.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
