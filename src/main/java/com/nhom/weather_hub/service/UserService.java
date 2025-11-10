package com.nhom.weather_hub.service;

import com.nhom.weather_hub.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    public User getCurrentUser();

}
