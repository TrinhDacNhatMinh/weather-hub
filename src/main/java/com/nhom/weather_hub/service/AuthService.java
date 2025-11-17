package com.nhom.weather_hub.service;

import com.nhom.weather_hub.dto.request.LoginRequest;
import com.nhom.weather_hub.dto.request.RefreshTokenRequest;
import com.nhom.weather_hub.dto.request.RegisterRequest;
import com.nhom.weather_hub.dto.response.AuthResponse;
import com.nhom.weather_hub.dto.response.RegisterResponse;
import com.nhom.weather_hub.dto.response.VerifyResponse;

public interface AuthService {

    public AuthResponse login(LoginRequest request);

    public AuthResponse refreshToken(RefreshTokenRequest request);

    public RegisterResponse register(RegisterRequest request);

    public VerifyResponse verifyEmail(String token);

}
