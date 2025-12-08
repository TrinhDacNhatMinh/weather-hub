package com.nhom.weather_hub.repository;

import com.nhom.weather_hub.entity.Threshold;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ThresholdRepository extends JpaRepository<Threshold, Long> {

    public Optional<Threshold> findByStationId(Long stationId);

}
