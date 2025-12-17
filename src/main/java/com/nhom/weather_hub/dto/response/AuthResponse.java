package com.nhom.weather_hub.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Authentication response containing generated tokens")
public record AuthResponse(

        @Schema(description = "JWT access token used for authorized requests",
                example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2NTk4NTM3MSwiZXhwIjoxNzY2MDcxNzcxfQ.mIk_QLjWufMo5dLWMvx4eewHJxiAGsJuYJKHGRl-ExM")
        String accessToken,

        @Schema(description = "JWT refresh token used to obtain new access tokens",
                example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2NTk4NTM3MSwiZXhwIjoxNzY2NTkwMTcxfQ.bDYzIyl1gPnn0IHl-6KM38bGEdJ8UzXRjJf8vOnxI5k")
        String refreshToken

) {
}
