package com.nhom.weather_hub.controller;

import com.nhom.weather_hub.dto.request.StationRequest;
import com.nhom.weather_hub.dto.response.PageResponse;
import com.nhom.weather_hub.dto.response.StationResponse;
import com.nhom.weather_hub.service.StationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stations")
@RequiredArgsConstructor
@Tag(name = "Station API",description = "APIs for managing weather stations: creating, updating, deleting, listing, and retrieving by user or API key.")
public class StationController {

    private final StationService stationService;

    @PostMapping
    @Operation(summary = "Create a new station", description = "Generate a new station and return the generated API key.")
    public ResponseEntity<String> createStation() {
        String response = stationService.createStation();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/batch")
    @Operation(summary = "Create multiple stations", description = "Create `n` new station at once. Returns a list of generated API key.")
    public ResponseEntity<List<String>> createStations(@RequestParam(defaultValue = "1") int n) {
        List<String> responses = stationService.createStations(n);
        return ResponseEntity.ok(responses);
    }

    @GetMapping
    @Operation(summary = "Get list of stations", description = "Retrieve a paginated list of stations with page and size parameters.")
    public ResponseEntity<PageResponse<StationResponse>> getStations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageResponse<StationResponse> responses = stationService.getStations(page, size);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get stations by user", description = "Retrieve all stations belonging to a specific user, with pagination support.")
    public ResponseEntity<PageResponse<StationResponse>> getStationsByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageResponse<StationResponse> responses = stationService.getStationsByUser(userId, page, size);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get station by ID", description = "Retrieve detailed information of a station using its ID.")
    public ResponseEntity<StationResponse> getStationById(@PathVariable Long id) {
        StationResponse response = stationService.getStationById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api-key/{apiKey}")
    @Operation(summary = "Get station by API key", description = "Retrieve station information associated with a given API key. Useful for device authentication.")
    public ResponseEntity<StationResponse> getStationByApiKey(@PathVariable String apiKey) {
        StationResponse response = stationService.getStationByApiKey(apiKey);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update station", description = "Update the details of a station using its ID and a StationRequest payload.")
    public ResponseEntity<StationResponse> updateStation(
            @PathVariable Long id,
            @Valid @RequestBody StationRequest updateRequest
    ) {
        StationResponse response = stationService.updateStation(id, updateRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete station", description = "Delete a station by its ID. Returns HTTP 204 No Content.")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        stationService.deleteStation(id);
        return ResponseEntity.noContent().build();
    }

}
