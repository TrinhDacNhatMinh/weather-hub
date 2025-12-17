package com.nhom.weather_hub.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Register request")
public record RegisterRequest(

        @NotBlank(message = "Full name cannot be blank")
        @Schema(description = "Full name of the user", example = "Nguyen Van A")
        String name,

        @NotBlank(message = "Username cannot be blank")
        @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
        @Schema(description = "Unique username used for login", example = "user123")
        String username,

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 6, message = "Password must contain at least 6 characters")
        @Schema(description = "User password", example = "abc123")
        String password,

        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Email must be valid")
        @Schema(description = "Valid email address used for verification and notifications", example = "ex@gmail.com")
        String email

) {
}
