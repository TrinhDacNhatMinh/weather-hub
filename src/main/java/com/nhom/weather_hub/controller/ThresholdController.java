package com.nhom.weather_hub.controller;

import com.nhom.weather_hub.dto.request.UpdateThresholdRequest;
import com.nhom.weather_hub.dto.response.ThresholdResponse;
import com.nhom.weather_hub.service.ThresholdService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/thresholds")
@RequiredArgsConstructor
public class ThresholdController {

    private final ThresholdService thresholdService;

    @GetMapping("/station/{stationId}")
    public ResponseEntity<ThresholdResponse> getByStation(@PathVariable Long stationId) {
        ThresholdResponse response = thresholdService.getByStationId(stationId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ThresholdResponse> updateThreshold(@PathVariable Long id, @RequestBody UpdateThresholdRequest request) {
        ThresholdResponse response = thresholdService.updateThreshold(id, request);
        return ResponseEntity.ok(response);
    }

}
