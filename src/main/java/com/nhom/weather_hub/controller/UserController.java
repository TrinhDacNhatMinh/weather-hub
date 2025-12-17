package com.nhom.weather_hub.controller;

import com.nhom.weather_hub.dto.request.UpdateUserRequest;
import com.nhom.weather_hub.dto.response.PageResponse;
import com.nhom.weather_hub.dto.response.UserResponse;
import com.nhom.weather_hub.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<PageResponse<UserResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageResponse<UserResponse> response = userService.getAllUsers(page, size);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/lock")
    public ResponseEntity<UserResponse> lockUser(@PathVariable Long id) {
        UserResponse response = userService.lockUser(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/unlock")
    public ResponseEntity<UserResponse> unlockUser(@PathVariable Long id) {
        UserResponse response = userService.unlockUser(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUserInfo(
            @PathVariable Long id,
            @RequestBody @Valid UpdateUserRequest request) {
        UserResponse response = userService.updateUserInfo(id, request);
        return ResponseEntity.ok(response);
    }

}
