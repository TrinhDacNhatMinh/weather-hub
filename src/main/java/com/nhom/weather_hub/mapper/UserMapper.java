package com.nhom.weather_hub.mapper;

import com.nhom.weather_hub.dto.response.UserResponse;
import com.nhom.weather_hub.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User entity) {
        if (entity == null) {
            return null;
        }

        UserResponse userResponse = new UserResponse();
        userResponse.setId(entity.getId());
        userResponse.setName(entity.getName());
        userResponse.setUsername(entity.getUsername());
        userResponse.setEmail(entity.getEmail());
        userResponse.setActive(entity.getActive());

        return userResponse;
    }
}
