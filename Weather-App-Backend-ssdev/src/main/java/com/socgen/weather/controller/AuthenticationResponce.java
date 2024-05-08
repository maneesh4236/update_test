package com.socgen.weather.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
public class AuthenticationResponce {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public AuthenticationResponce(String token) {
        this.token = token;
    }

    public AuthenticationResponce() {
    }

    @Override
    public String toString() {
        return "AuthenticationResponce{" +
                "token='" + token + '\'' +
                '}';
    }
}
