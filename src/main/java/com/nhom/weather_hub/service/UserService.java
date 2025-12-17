package com.nhom.weather_hub.service;

import com.nhom.weather_hub.dto.request.UpdateUserRequest;
import com.nhom.weather_hub.dto.response.PageResponse;
import com.nhom.weather_hub.dto.response.UserResponse;
import com.nhom.weather_hub.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User getCurrentUser();

    PageResponse<UserResponse> getAllUsers(int page, int size);

    UserResponse lockUser(Long id);

    UserResponse unlockUser(Long id);

    UserResponse updateUserInfo(Long id, UpdateUserRequest request);

}
