package com.nhom.weather_hub.controller;

import com.nhom.weather_hub.dto.response.AlertResponse;
import com.nhom.weather_hub.dto.response.PageResponse;
import com.nhom.weather_hub.service.AlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
@Tag(name = "Alerts", description = "Manage alerts in the system, including retrieval, marking as seen, and deletion.")
public class AlertController {

    private final AlertService alertService;

    @GetMapping("/{id}")
    @Operation(
            summary = "Get alert by ID",
            description = "Retrieve detailed information of a specific alert by its ID. Requires authentication."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the alert"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user not logged in"),
            @ApiResponse(responseCode = "404", description = "Alert not found with the given ID"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<AlertResponse> getAlert(@PathVariable Long id) {
        AlertResponse response = alertService.getAlert(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @Operation(
            summary = "Get alerts of the current user",
            description = "Retrieve paginated alerts for the currently authenticated user. Requires authentication."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved alerts"),
            @ApiResponse(responseCode = "400", description = "Invalid page or size parameter"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user not logged in"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PageResponse<AlertResponse>> getMyAlerts(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size
    ) {
        PageResponse<AlertResponse> response = alertService.getAlertsOfCurrentUser(page, size);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/seen")
    @Operation(
            summary = "Mark an alert as seen",
            description = "Mark a specific alert as seen by its ID. Requires authentication."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Alert marked as seen successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user not logged in"),
            @ApiResponse(responseCode = "404", description = "Alert not found with the given ID"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<AlertResponse> markAsSeen(@PathVariable Long id) {
        AlertResponse response = alertService.markAsSeen(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete an alert by ID",
            description = "Delete a specific alert by its ID. Requires authentication."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Alert deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user not logged in"),
            @ApiResponse(responseCode = "404", description = "Alert not found with the given ID"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteAlert(@PathVariable Long id) {
        alertService.deleteAlert(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me")
    @Operation(
            summary = "Delete all alerts of the current user",
            description = "Delete all alerts associated with the currently authenticated user. Requires authentication."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "All alerts deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user not logged in"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteMyAlerts() {
        alertService.deleteAlertsOfCurrentUser();
        return ResponseEntity.noContent().build();
    }

}
