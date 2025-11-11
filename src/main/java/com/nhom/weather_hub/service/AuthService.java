package com.nhom.weather_hub.service;

import com.nhom.weather_hub.dto.request.LoginRequest;
import com.nhom.weather_hub.dto.response.AuthResponse;

public interface AuthService {

    public AuthResponse login(LoginRequest request);

    public AuthResponse refreshToken(String refreshToken);

}
