package com.nhom.weather_hub.repository;

import com.nhom.weather_hub.entity.WeatherData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {

    Page<WeatherData> findByStationIdAndRecordAtBetween(Long stationId, Instant from, Instant to, Pageable pageable);

    Optional<WeatherData> findFirstByStationIdOrderByRecordAtDesc(Long stationId);

    void deleteByStationId(Long stationId);

}
