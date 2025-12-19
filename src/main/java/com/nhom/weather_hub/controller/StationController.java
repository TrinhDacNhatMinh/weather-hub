package com.nhom.weather_hub.controller;

import com.nhom.weather_hub.dto.request.AddStationRequest;
import com.nhom.weather_hub.dto.request.UpdateStationRequest;
import com.nhom.weather_hub.dto.response.PageResponse;
import com.nhom.weather_hub.dto.response.StationResponse;
import com.nhom.weather_hub.service.StationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stations")
@RequiredArgsConstructor
@Tag(name = "Stations", description = "Manage stations: create, update, delete, and retrieve station information.")
public class StationController {

    private final StationService stationService;

    @PostMapping
    @Operation(
            summary = "Create a new station",
            description = "Generates a new station with a unique API key and default settings. Requires admin permission."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Station created successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user not logged in"),
            @ApiResponse(responseCode = "403", description = "Forbidden, admin permission required"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> createStation() {
        String response = stationService.createStation();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/batch")
    @Operation(
            summary = "Create multiple stations",
            description = "Create a batch of stations, each with a generated API key. Requires admin permission."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Stations created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameter"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user not logged in"),
            @ApiResponse(responseCode = "403", description = "Forbidden, admin permission required"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<String>> createStationsBatch(@RequestParam(defaultValue = "1") @Min(1) int n) {
        List<String> responses = stationService.createStationsBatch(n);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }

    @PutMapping("/attach")
    @Operation(
            summary = "Attach station to current user",
            description = "Attach an existing station to the currently authenticated user using API key."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Station successfully attached to user"),
            @ApiResponse(responseCode = "400", description = "Invalid add station request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user not logged in"),
            @ApiResponse(responseCode = "404", description = "Station not found"),
            @ApiResponse(responseCode = "409", description = "Station already attached to another user"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<StationResponse> addStationToUser(@RequestBody @Valid AddStationRequest request) {
        StationResponse response = stationService.addStationToUser(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/me/stations")
    @Operation(
            summary = "Get stations of current user",
            description = "Retrieve a paginated list of stations owned by the currently authenticated user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved stations"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user not logged in"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PageResponse<StationResponse>> getMyStations(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size
    ) {
        PageResponse<StationResponse> response = stationService.getStationsOfCurrentUser(page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/{userId}")
    @Operation(
            summary = "Get stations by user ID",
            description = "Retrieve a paginated list of stations belonging to a specific user. Requires admin permission."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved stations"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user not logged in"),
            @ApiResponse(responseCode = "403", description = "Forbidden, admin permission required"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PageResponse<StationResponse>> getStationByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size
    ) {
        PageResponse<StationResponse> response = stationService.getStationsByUserId(userId, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/public")
    @Operation(
            summary = "Get public stations",
            description = "Retrieve a paginated list of public stations."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved public stations"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user not logged in"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PageResponse<StationResponse>> getPublicStations(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size
    ) {
        PageResponse<StationResponse> response = stationService.getPublicStations(page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @Operation(
            summary = "Get all stations",
            description = "Retrieve a paginated list of all stations in the system. Requires admin permission."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved stations"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user not logged in"),
            @ApiResponse(responseCode = "403", description = "Forbidden, admin permission required"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PageResponse<StationResponse>> getAllStations(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size
    ) {
        PageResponse<StationResponse> response = stationService.getAllStations(page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get station by ID",
            description = "Retrieve detailed information of a station by its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved station"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user not logged in"),
            @ApiResponse(responseCode = "403", description = "Forbidden, access denied"),
            @ApiResponse(responseCode = "404", description = "Station not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<StationResponse> getStationById(@PathVariable Long id) {
        StationResponse response = stationService.getStationById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api-key/{apiKey}")
    @Operation(
            summary = "Get station by API key",
            description = "Get station by API key."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved station"),
            @ApiResponse(responseCode = "400", description = "Invalid API key"),
            @ApiResponse(responseCode = "404", description = "Station not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<StationResponse> getStationByApiKey(@PathVariable @NotBlank String apiKey) {
        StationResponse response = stationService.getStationByApiKey(apiKey);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update station",
            description = "Update station information. Only the station owner or admin can perform this action."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Station successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid update station request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user not logged in"),
            @ApiResponse(responseCode = "403", description = "Forbidden, access denied"),
            @ApiResponse(responseCode = "404", description = "Station not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<StationResponse> updateStation(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStationRequest request
    ) {
        StationResponse response = stationService.updateStation(id, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/public")
    @Operation(
            summary = "Update station public status",
            description = "Enable or disable public sharing of a station."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Station sharing status updated"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user not logged in"),
            @ApiResponse(responseCode = "403", description = "Forbidden, access denied"),
            @ApiResponse(responseCode = "404", description = "Station not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<StationResponse> updateStationSharing(@PathVariable Long id) {
        StationResponse response = stationService.updateStationSharing(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/user")
    @Operation(
            summary = "Detach station from user",
            description = "Remove the association between a station and its current user without deleting the station."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Station successfully detached from user"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user not logged in"),
            @ApiResponse(responseCode = "403", description = "Forbidden, access denied"),
            @ApiResponse(responseCode = "404", description = "Station not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> detachStationFromUser(@PathVariable Long id) {
        stationService.detachStationFromUser(id);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete station",
            description = "Permanently delete a station from the system. Requires admin permission."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Station successfully deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, user not logged in"),
            @ApiResponse(responseCode = "403", description = "Forbidden, admin permission required"),
            @ApiResponse(responseCode = "404", description = "Station not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        stationService.deleteStation(id);
        return ResponseEntity.noContent().build();
    }

}
