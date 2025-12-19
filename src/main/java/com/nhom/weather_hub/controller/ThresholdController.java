package com.nhom.weather_hub.controller;

import com.nhom.weather_hub.dto.request.UpdateThresholdRequest;
import com.nhom.weather_hub.dto.response.ThresholdResponse;
import com.nhom.weather_hub.service.ThresholdService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/thresholds")
@RequiredArgsConstructor
@Tag(name = "Thresholds", description = "Manage threshold configurations for weather stations.")
public class ThresholdController {

    private final ThresholdService thresholdService;

    @PutMapping("/{id}")
    @Operation(
            summary = "Update threshold",
            description = "Update threshold configuration by threshold ID." +
                    "Only the station owner can perform this action."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Threshold updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid threshold update request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user not logged in"),
            @ApiResponse(responseCode = "403", description = "Forbidden, access denied"),
            @ApiResponse(responseCode = "404", description = "Threshold not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ThresholdResponse> updateThreshold(
            @PathVariable Long id,
            @RequestBody @Valid UpdateThresholdRequest request) {
        ThresholdResponse response = thresholdService.updateThreshold(id, request);
        return ResponseEntity.ok(response);
    }

}
