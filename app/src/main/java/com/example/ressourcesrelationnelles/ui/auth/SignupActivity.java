package com.example.ressourcesrelationnelles.ui.auth;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class SignupActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName, etEmail, etPassword;
    private ProgressBar pbStrength;
    private TextView tvError, reqLength, reqUpper, reqLower, reqDigit, reqSpecial;
    private Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initViews();
        initPasswordValidation();
    }

    private void initViews() {
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        pbStrength = findViewById(R.id.pbStrength);
        tvError = findViewById(R.id.tvError);
        btnSignup = findViewById(R.id.btnSignup);
        reqLength = findViewById(R.id.req_length);
        reqUpper = findViewById(R.id.req_upper);
        reqLower = findViewById(R.id.req_lower);
        reqDigit = findViewById(R.id.req_digit);
        reqSpecial = findViewById(R.id.req_special);

        validatePassword(etPassword.getText().toString().trim());


        Button btnGoLogin = findViewById(R.id.btnGoLogin);
        btnGoLogin.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        btnSignup.setOnClickListener(v -> {
            var firstname = etFirstName.getText().toString().trim();
            var lastname = etLastName.getText().toString().trim();
            var email = etEmail.getText().toString().trim();
            var password = etPassword.getText().toString().trim();

            validatePassword(password);

            Register(firstname, lastname, email, password);
        });
    }

    private void initPasswordValidation() {
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { /* not used */ }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { /* not used */ }
            @Override
            public void afterTextChanged(Editable s) {
                validatePassword(s.toString());
            }
        });
    }

    // Vérifie la force du mot de passe
    private void validatePassword(String password) {
        int strength = 0;

// Critères
        boolean hasLength = password.length() >= 8;
        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasSpecial = password.matches(".*[@$!%*?&].*");

// Couleurs
        int colorOK = Color.parseColor("#28a745");
        int colorNOK = Color.parseColor("#888888");

// MàJ affichage visuel
        reqLength.setTextColor(hasLength ? colorOK : colorNOK);
        reqUpper.setTextColor(hasUpper ? colorOK : colorNOK);
        reqLower.setTextColor(hasLower ? colorOK : colorNOK);
        reqDigit.setTextColor(hasDigit ? colorOK : colorNOK);
        reqSpecial.setTextColor(hasSpecial ? colorOK : colorNOK);

// Calcul force
        if (hasLength) strength++;
        if (hasUpper) strength++;
        if (hasLower) strength++;
        if (hasDigit) strength++;
        if (hasSpecial) strength++;

        int percentage = (int)((strength / 5.0f) * 100f);
        pbStrength.setProgress(percentage);

        if (percentage < 40) {
            pbStrength.getProgressDrawable().setColorFilter(
                    Color.parseColor("#FF4944"), PorterDuff.Mode.SRC_IN);
        } else if (percentage < 80) {
            pbStrength.getProgressDrawable().setColorFilter(
                    Color.parseColor("#F2BB16"), PorterDuff.Mode.SRC_IN);
        } else {
            pbStrength.getProgressDrawable().setColorFilter(
                    Color.parseColor("#00B23C"), PorterDuff.Mode.SRC_IN);
        }

        btnSignup.setEnabled(percentage == 100);

    }

    private void Register(String firstName, String lastName, String email, String password) {
        tvError.setVisibility(View.GONE);
        IApiService api = ApiServiceBuilder.getApiService();

        // Construction du body en tant que Map (correspondant à @RequestBody côté Spring)
        Map<String, String> registerData = new HashMap<>();
        registerData.put("firstName", firstName);
        registerData.put("lastName", lastName);
        registerData.put("email", email);
        registerData.put("password", password);

        // Appel typé via méthode dédiée
        api.signup(registerData).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ApiAuthResponse> call, Response<ApiAuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiAuthResponse auth = response.body();

                    TokenManager tokenManager = new TokenManager();
                    tokenManager.saveToken(auth.token);
                    tokenManager.saveUser(auth.user);

                    Toast.makeText(SignupActivity.this, "Inscription réussie !", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignupActivity.this, MainActivity.class));
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
                Toast.makeText(SignupActivity.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
