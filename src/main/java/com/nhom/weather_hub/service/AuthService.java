package com.nhom.weather_hub.service;

import com.nhom.weather_hub.dto.request.LoginRequest;

public interface AuthService {

    public String login(LoginRequest request);

}
