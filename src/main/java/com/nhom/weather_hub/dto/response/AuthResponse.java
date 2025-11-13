package com.nhom.weather_hub.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Authentication response containing generated tokens.")
public class AuthResponse {
    @Schema(description = "JWT access token used for authorized requests.", example = "eyJhbGciOiJIUzI1...")
    private String accessToken;

    @Schema(description = "JWT refresh token used to obtain new access tokens.", example = "eyJhbGciOiJIUzI1...")
    private String refreshToken;
}
