package com.nhom.weather_hub.repository;

import com.nhom.weather_hub.entity.WeatherData;
import com.nhom.weather_hub.projection.DailyWeatherSummaryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {

    @Query(value = """
            SELECT
                DATE(record_at) AS date,
            
                MIN(temperature)           AS minTemperature,
                MAX(temperature)           AS maxTemperature,
                ROUND(AVG(temperature), 2) AS avgTemperature,
            
                MIN(humidity)              AS minHumidity,
                MAX(humidity)              AS maxHumidity,
                ROUND(AVG(humidity), 2)    AS avgHumidity,
            
                MIN(dust)                  AS minDust,
                MAX(dust)                  AS maxDust,
                ROUND(AVG(dust), 2)        AS avgDust,
            
                MIN(wind_speed)            AS minWindSpeed,
                MAX(wind_speed)            AS maxWindSpeed,
                ROUND(AVG(wind_speed), 2)  AS avgWindSpeed,
            
                ROUND(SUM(rainfall), 2)    AS totalRainfall
            FROM weather_data
            WHERE station_id = :stationId AND record_at >= :from AND record_at < :to
            GROUP BY DATE(record_at)
            ORDER BY DATE(record_at) ASC
            """, nativeQuery = true)
    List<DailyWeatherSummaryProjection> findDailySummary(
            @Param("stationId") Long stationId,
            @Param("from") Instant from,
            @Param("to") Instant to
    );

    void deleteByStationId(Long stationId);

}
