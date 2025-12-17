package com.nhom.weather_hub.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response object representing a user")
public record UserResponse(

        @Schema(description = "User ID", example = "7")
        Long id,

        @Schema(description = "Full name of the user", example = "Nguyen Van A")
        String name,

        @Schema(description = "Username used for login", example = "user123")
        String username,

        @Schema(description = "Email address of the user", example = "ex@gmail.com")
        String email,

        @Schema(description = "Whether the user account is active", example = "true")
        Boolean active,

        @Schema(description = "Number of stations owned by the user", example = "3")
        Long stationCount

) {
}
