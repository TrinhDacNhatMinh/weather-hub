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

    LoginResponse login(LoginRequest request);

    void logout(String authHeader);

    AuthResponse refreshToken(RefreshTokenRequest request);

    RegisterResponse register(RegisterRequest request);

    VerifyResponse verifyEmail(String token);

    void changePassword(ChangePasswordRequest request);

}
