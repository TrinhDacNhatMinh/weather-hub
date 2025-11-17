package com.nhom.weather_hub.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema
public class RegisterRequest {

    @NotBlank
    @Schema(
            description = "Full name of the user.",
            example = "Nguyen Van A"
    )
    private String name;

    @NotBlank
    @Size(min = 3, max = 20)
    @Schema(
            description = "Unique username used for login. Must be between 3 and 20 characters.",
            example = "user123"
    )
    private String username;

    @NotBlank
    @Size(min = 6)
    @Schema(
            description = "User password. Must contain at least 6 characters.",
            example = "abc123"
    )
    private String password;

    @NotBlank
    @Email
    @Schema(
            description = "Valid email address used for verification and notifications.",
            example = "example@gmail.com"
    )
    private String email;

}
