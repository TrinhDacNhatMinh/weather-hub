package com.nhom.weather_hub.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserRequest {

    @NotBlank
    private String name;

}
