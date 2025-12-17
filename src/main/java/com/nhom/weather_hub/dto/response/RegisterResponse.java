package com.nhom.weather_hub.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response returned after user registration")
public record RegisterResponse(

        @Schema(description = "ID of the newly created user", example = "123")
        Long userId,

        @Schema(description = "Email of the registered user", example = "user@example.com")
        String email,

        @Schema(description = "Activation status of the user account", example = "false")
        boolean active,

        @Schema(
                description = "Message indicating registration result",
                example = "User registered successfully. Please check your email to verify your account."
        )
        String message

) {
}
