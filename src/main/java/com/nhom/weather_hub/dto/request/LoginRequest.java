package com.nhom.weather_hub.dto.request;

import com.nhom.weather_hub.domain.enums.AccessChannel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Login request")
public record LoginRequest (

        @NotBlank(message = "Username cannot be blank")
        @Schema(description = "The username of the account", example = "user123")
        String username,

        @NotBlank(message = "Password cannot be blank")
        @Schema(description = "The password of the account", example = "qwe123")
        String password,

        @NotNull(message = "Access channel must be provided")
        @Schema(description = "Access channel of the login request", example = "MOBILE")
        AccessChannel accessChannel

) {
}
