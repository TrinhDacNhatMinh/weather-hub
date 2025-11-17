package com.nhom.weather_hub.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request object used to obtain a new access token using a valid refresh token.")
public class RefreshTokenRequest {
    @NotBlank
    @Schema(
            description = "The refresh token issued to the client during login.",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String refreshToken;
}
