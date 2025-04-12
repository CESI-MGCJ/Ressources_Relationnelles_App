package com.example.ressourcesrelationnelles.models;

import com.example.ressourcesrelationnelles.models.entities.User;

public class ApiAuthResponse {
    public String token;
    public User user;

    // getters/setters

    public ApiAuthResponse(String token, User user) {
        this.token = token;
        this.user = user;
    }
}

