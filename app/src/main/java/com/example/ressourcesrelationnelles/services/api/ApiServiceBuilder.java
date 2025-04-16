package com.example.ressourcesrelationnelles.services.api;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.example.ressourcesrelationnelles.App;
import com.example.ressourcesrelationnelles.ui.auth.LoginActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiServiceBuilder {

    private static Retrofit retrofit = null;

    public static IApiService getApiService() {
        TokenManager tokenManager = new TokenManager();

        // Intercepteur d'autorisation et Accept: application/json
        Interceptor authInterceptor = chain -> {
            Request original = chain.request();
            Request.Builder builder = original.newBuilder()
                    .header("Accept", "application/json");  // ← Clé pour éviter le 406

            // Ajout du token si disponible
            String token = tokenManager.getToken();
            if (token != null) {
                builder.header("Authorization", "Bearer " + token);
            }

            return chain.proceed(builder.build());
        };

        // Gestion automatique des erreurs d'authentification
        Interceptor errorInterceptor = chain -> {
            Response response = chain.proceed(chain.request());

            if (response.code() == 403) {
                Toast.makeText(App.getInstance(), "Authorisations insuffisantes", Toast.LENGTH_SHORT).show();
            }

            return response;
        };

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Client HTTP configuré
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(authInterceptor)
                .addInterceptor(errorInterceptor)
                .addInterceptor(logging)
                .build();



        // Retrofit instance
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd") // <- correspond exactement à "2025-04-12"
                    .create();


            retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.223.66:8080/api/") // ← à adapter si nécessaire
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
        }

        return retrofit.create(IApiService.class);
    }
}
