package com.nhom.weather_hub.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Response returned after email verification")
public record VerifyResponse(

        @Schema(description = "Email of the verified user", example = "user@example.com")
        String email,

        @Schema(description = "Indicates whether the account has been verified", example = "true")
        boolean verified,

        @Schema(description = "Timestamp when the account was verified", example = "2025-11-17T09:00:00Z")
        Instant verifiedAt,

        @Schema(description = "Message indicating verification result", example = "Account verified successfully")
        String message

) {
}
