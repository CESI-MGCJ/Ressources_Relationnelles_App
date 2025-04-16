package com.example.ressourcesrelationnelles.services.api;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.ressourcesrelationnelles.App;
import com.example.ressourcesrelationnelles.models.entities.User;
import com.google.gson.Gson;

import java.time.LocalDate;

public class TokenManager {

    private static final String PREF_NAME = "auth_prefs";
    private static final String KEY_TOKEN = "jwt_token";
    private static final String KEY_TOKEN_EXPIRATION = "jwt_token_expiration";

    private static final String KEY_USER = "user_data";

    private final SharedPreferences prefs;

    public TokenManager() {
        prefs = App.getInstance().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public boolean isLogged(){
        return getToken() != null && getUser() != null;
    }

    public void saveToken(String token) {
        prefs.edit().putString(KEY_TOKEN, token).apply();
        prefs.edit().putString(KEY_TOKEN_EXPIRATION, LocalDate.now().plusMonths(3).toString()).apply();
    }

    public String getToken() {
        if(!isTokenValid()) return null;
        return prefs.getString(KEY_TOKEN, null);
    }

    public void clearToken() {
        prefs.edit().remove(KEY_TOKEN).apply();
        prefs.edit().remove(KEY_TOKEN_EXPIRATION).apply();
    }

    public void saveUser(User user) {
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        prefs.edit().putString(KEY_USER, userJson).apply();
    }

    public User getUser() {
        if(!isTokenValid()) return null;
        String userJson = prefs.getString(KEY_USER, null);
        if (userJson == null) return null;
        return new Gson().fromJson(userJson, User.class);
    }

    public void clearUser() {
        prefs.edit().remove(KEY_USER).apply();
    }

    private boolean isTokenValid() {
        String expirationDateString = prefs.getString(KEY_TOKEN_EXPIRATION, null);
        if (expirationDateString == null) {
            clearUser();
            clearToken();
            return false; // Pas de date d'expiration enregistrée
        }

        LocalDate expirationDate = LocalDate.parse(expirationDateString);
        LocalDate today = LocalDate.now();

        // Si aujourd'hui est avant ou le même jour que la date d'expiration, le token est valide
        if(today.isAfter(expirationDate)){
            clearUser();
            clearToken();
            return false;
        }
        else
            return true;

    }
}

