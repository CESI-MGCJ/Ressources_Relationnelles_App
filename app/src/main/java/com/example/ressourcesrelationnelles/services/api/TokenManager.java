package com.example.ressourcesrelationnelles.services.api;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.ressourcesrelationnelles.App;
import com.example.ressourcesrelationnelles.models.entities.User;
import com.google.gson.Gson;

public class TokenManager {

    private static final String PREF_NAME = "auth_prefs";
    private static final String KEY_TOKEN = "jwt_token";
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
    }

    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    public void clearToken() {
        prefs.edit().remove(KEY_TOKEN).apply();
    }

    public boolean hasToken() {
        return getToken() != null;
    }

    public void saveUser(User user) {
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        prefs.edit().putString(KEY_USER, userJson).apply();
    }

    public User getUser() {
        String userJson = prefs.getString(KEY_USER, null);
        if (userJson == null) return null;
        return new Gson().fromJson(userJson, User.class);
    }

    public void clearUser() {
        prefs.edit().remove(KEY_USER).apply();
    }
}

