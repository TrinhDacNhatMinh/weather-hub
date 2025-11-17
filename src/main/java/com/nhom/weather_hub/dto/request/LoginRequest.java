package com.nhom.weather_hub.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Login request payload containing user credentials.")
public class LoginRequest {

    @NotBlank
    @Schema(description = "The username of the account.", example = "user123")
    private String username;

    @NotBlank
    @Schema(description = "The password of the account.", example = "abc456")
    private String password;

}
