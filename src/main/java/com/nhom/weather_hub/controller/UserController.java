package com.nhom.weather_hub.controller;

import com.nhom.weather_hub.dto.request.UpdateUserRequest;
import com.nhom.weather_hub.dto.response.PageResponse;
import com.nhom.weather_hub.dto.response.UserResponse;
import com.nhom.weather_hub.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "Manage user accounts, lock/unlock, and update user information.")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(
            summary = "Get paginated list of users",
            description = "Retrieve a paginated list of all users. Requires admin permission."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user not logged in"),
            @ApiResponse(responseCode = "403", description = "Forbidden, admin permission required"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PageResponse<UserResponse>> getUsers(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size
    ) {
        PageResponse<UserResponse> response = userService.getUsers(page, size);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/lock")
    @Operation(
            summary = "Lock user account",
            description = "Lock the account of a user by ID. Only admins can perform this action."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User account locked successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user not logged in"),
            @ApiResponse(responseCode = "403", description = "Forbidden, admin permission required"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserResponse> lockUser(@PathVariable Long id) {
        UserResponse response = userService.lockUser(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/unlock")
    @Operation(
            summary = "Unlock user account",
            description = "Unlock the account of a user by ID. Only admins can perform this action."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User account unlocked successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user not logged in"),
            @ApiResponse(responseCode = "403", description = "Forbidden, admin permission required"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserResponse> unlockUser(@PathVariable Long id) {
        UserResponse response = userService.unlockUser(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update user information",
            description = "Update information of a user by ID. Only the user themselves can perform this action."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid update user request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user not logged in"),
            @ApiResponse(responseCode = "403", description = "Forbidden, access denied"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @RequestBody @Valid UpdateUserRequest request) {
        UserResponse response = userService.updateUser(id, request);
        return ResponseEntity.ok(response);
    }

}
