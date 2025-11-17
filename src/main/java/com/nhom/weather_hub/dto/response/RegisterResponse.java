package com.nhom.weather_hub.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Response returned after user registration")
public class RegisterResponse {

    @Schema(description = "ID of the newly created user", example = "123")
    private Long userId;

    @Schema(description = "Email of the registered user", example = "user@example.com")
    private String email;

    @Schema(description = "Activation status of the user account", example = "false")
    private boolean active;

    @Schema(description = "Message indicating registration result", example = "User registered successfully. Please check your email to verify your account.")
    private String message;

}
