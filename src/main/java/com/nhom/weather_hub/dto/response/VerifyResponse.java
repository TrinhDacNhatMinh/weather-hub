package com.nhom.weather_hub.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
@Schema(description = "Response returned after email verification")
public class VerifyResponse {

    @Schema(description = "Email of the verified user", example = "user@example.com")
    private String email;

    @Schema(description = "Indicates whether the account has been verified", example = "true")
    private boolean verified;

    @Schema(description = "Timestamp when the account was verified", example = "2025-11-17T09:00:00Z")
    private Instant verifiedAt;

    @Schema(description = "Message indicating verification result", example = "Account verified successfully")
    private String message;

}
