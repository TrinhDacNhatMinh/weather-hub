package com.nhom.weather_hub.controller;

import com.nhom.weather_hub.dto.request.ChangePasswordRequest;
import com.nhom.weather_hub.dto.request.LoginRequest;
import com.nhom.weather_hub.dto.request.RefreshTokenRequest;
import com.nhom.weather_hub.dto.request.RegisterRequest;
import com.nhom.weather_hub.dto.response.AuthResponse;
import com.nhom.weather_hub.dto.response.LoginResponse;
import com.nhom.weather_hub.dto.response.RegisterResponse;
import com.nhom.weather_hub.dto.response.VerifyResponse;
import com.nhom.weather_hub.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user registration, login, token management, and password changes.")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(
            summary = "Register a new user",
            description = "Creates a new account with the provided registration details."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid registration request"),
            @ApiResponse(responseCode = "409", description = "Conflict username or email already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest request) {
        RegisterResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/verify")
    @Operation(
            summary = "Verify user email",
            description = "Verify a user's email using the token sent to their email address."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Email verified successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or expired token"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<VerifyResponse> verifyEmail(@RequestParam("token") @NotBlank String token) {
        VerifyResponse response = authService.verifyEmail(token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    @Operation(
            summary = "Login",
            description = "Authenticate a user with username and password. Returns JWT access token, refresh token, and user information."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid username or password"),
            @ApiResponse(responseCode = "403", description = "Account is not active or login channel not allowed"),
            @ApiResponse(responseCode = "500", description = "Internal server error")

    })
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    @Operation(
            summary = "Refresh JWT tokens",
            description = "Refresh the access token using a valid refresh token. Returns new access and refresh tokens."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tokens refreshed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Refresh token invalid or expired"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody @Valid RefreshTokenRequest request) {
        AuthResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Operation(
            summary = "Logout",
            description = "Invalidate the refresh token provided in the Authorization header."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Logout successful, refresh token deleted"),
            @ApiResponse(responseCode = "401", description = "Invalid or missing Authorization header"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader) {
        authService.logout(authHeader);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/change-password")
    @Operation(
            summary = "Change user password",
            description = "Change the password of the currently authenticated user. Requires old password verification."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Invalid or missing Authorization header"),
            @ApiResponse(responseCode = "403", description = "User account is not active"),
            @ApiResponse(responseCode = "422", description = "Old password does not match"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        authService.changePassword(request);
        return ResponseEntity.noContent().build();
    }

}
