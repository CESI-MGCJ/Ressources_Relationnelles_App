package com.example.ressourcesrelationnelles.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ressourcesrelationnelles.MainActivity;
import com.example.ressourcesrelationnelles.R;
import com.example.ressourcesrelationnelles.models.ApiAuthResponse;
import com.example.ressourcesrelationnelles.services.api.ApiServiceBuilder;
import com.example.ressourcesrelationnelles.services.api.IApiService;
import com.example.ressourcesrelationnelles.services.api.TokenManager;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private TextView tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Le layout ci-dessus

        initViews();
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        tvError = findViewById(R.id.tvError);

        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnSignup = findViewById(R.id.btnSignup);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            Login(email, password);
        });

        btnSignup.setOnClickListener(v -> {
            startActivity(new Intent(this, SignupActivity.class));
        });
    }

    private void Login(String email, String password) {
        tvError.setVisibility(View.GONE);
        IApiService api = ApiServiceBuilder.getApiService();

        Map<String, String> loginData = new HashMap<>();
        loginData.put("email", email);
        loginData.put("password", password);

        api.login(loginData).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ApiAuthResponse> call, Response<ApiAuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiAuthResponse auth = response.body();

                    TokenManager tokenManager = new TokenManager();
                    tokenManager.saveToken(auth.token);
                    tokenManager.saveUser(auth.user);

                    Toast.makeText(LoginActivity.this, "Connexion réussie !", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    try {
                        tvError.setText(response.errorBody().string());
                    } catch (Exception e) {
                        tvError.setText("Une erreur est survenue. Veuillez réessayer plus tard");
                    } finally {
                        tvError.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiAuthResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}