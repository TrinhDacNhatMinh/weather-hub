package com.nhom.weather_hub.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Change password request")
public record ChangePasswordRequest(

        @NotBlank(message = "Old password cannot be blank")
        @Schema(description = "Current password of the user", example = "oldPassword123")
        String oldPassword,

        @NotBlank(message = "New password cannot be blank")
        @Schema(description = "New password to be set", example = "newPassword456")
        String newPassword

) {
}
