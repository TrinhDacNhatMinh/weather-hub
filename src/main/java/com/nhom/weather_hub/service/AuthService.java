package com.nhom.weather_hub.service;

import com.nhom.weather_hub.dto.request.ChangePasswordRequest;
import com.nhom.weather_hub.dto.request.LoginRequest;
import com.nhom.weather_hub.dto.request.RefreshTokenRequest;
import com.nhom.weather_hub.dto.request.RegisterRequest;
import com.nhom.weather_hub.dto.response.AuthResponse;
import com.nhom.weather_hub.dto.response.LoginResponse;
import com.nhom.weather_hub.dto.response.RegisterResponse;
import com.nhom.weather_hub.dto.response.VerifyResponse;

public interface AuthService {

    RegisterResponse register(RegisterRequest request);

    VerifyResponse verifyEmail(String token);

    LoginResponse login(LoginRequest request);

    AuthResponse refreshToken(RefreshTokenRequest request);

    void logout(String authHeader);

    void changePassword(ChangePasswordRequest request);

}
