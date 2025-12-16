package com.nhom.weather_hub.domain.policy;

import com.nhom.weather_hub.domain.enums.AccessChannel;
import com.nhom.weather_hub.entity.Role;
import com.nhom.weather_hub.entity.User;
import com.nhom.weather_hub.exception.business.LoginChannelNotAllowedException;

public class LoginPolicy {
    public static void validate(User user, AccessChannel channel) {
        Role.RoleName role = user.getRole().getName();

        if (role == Role.RoleName.ROLE_ADMIN && channel == AccessChannel.MOBILE) {
            throw new LoginChannelNotAllowedException(
                    "Admin is not allowed to login from mobile"
            );
        }

        if (role == Role.RoleName.ROLE_USER && channel == AccessChannel.WEB) {
            throw new LoginChannelNotAllowedException(
                    "User is not allowed to login from web"
            );
        }
    }
}
