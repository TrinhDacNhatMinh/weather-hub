package com.nhom.weather_hub.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "Update user information")
public record UpdateUserRequest(

        @Size(min = 1, message = "Name cannot be empty if provided")
        @Schema(
                description = "Full name of the user. Optional but cannot be empty if provided",
                example = "Nguyen Van A",
                nullable = true
        )
        String name

) {
}
