package com.nhom.weather_hub.mapper;

import com.nhom.weather_hub.dto.response.UserResponse;
import com.nhom.weather_hub.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User entity, Long stationCount) {
        if (entity == null) {
            return null;
        }

        return new UserResponse(
                entity.getId(),
                entity.getName(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getActive(),
                stationCount
        );
    }

}
