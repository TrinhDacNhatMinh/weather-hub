package com.nhom.weather_hub.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Refresh token request")
public record RefreshTokenRequest(
        @NotBlank(message = "Refresh token cannot be blank")
        @Schema(
                description = "The refresh token issued to the client during login",
                example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0aHVvbmciLCJpYXQiOjE3NjU5Njc3NDcsImV4cCI6MTc2NjU3MjU0N30.uR_4sRpu_Bu0VsQUPzah1Lld1U0JawhmjhF8InkP2G8"
        )
        String refreshToken
) {
}
