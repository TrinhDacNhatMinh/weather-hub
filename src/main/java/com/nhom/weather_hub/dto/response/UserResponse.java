package com.nhom.weather_hub.dto.response;

import lombok.Data;

@Data
public class UserResponse {

    private Long id;

    private String name;

    private String username;

    private String email;

    private Boolean active;

    private Long stationCount;

}
