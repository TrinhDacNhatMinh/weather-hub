package com.nhom.weather_hub.repository;

import com.nhom.weather_hub.entity.WeatherData;
import com.nhom.weather_hub.projection.DailyWeatherSummaryProjection;
import com.nhom.weather_hub.projection.HourWeatherSummaryProjection;
import com.nhom.weather_hub.projection.StationAvgTemperatureProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

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
            
                ROUND(SUM(rainfall / 60), 2) AS totalRainfall
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

    @Query(value = """
                SELECT
                    DATE_FORMAT(record_at, '%Y-%m-%d %H:00:00') AS hour,
            
                    ROUND(AVG(temperature), 2) AS avgTemperature,
                    ROUND(AVG(humidity), 2)    AS avgHumidity,
                    ROUND(AVG(wind_speed), 2)  AS avgWindSpeed,
                    ROUND(AVG(dust), 2)        AS avgDust,
            
                    ROUND(SUM(rainfall / 60), 2) AS totalRainfall
                FROM weather_data
                WHERE station_id = :stationId
                  AND record_at >= :from
                  AND record_at < :to
                GROUP BY DATE_FORMAT(record_at, '%Y-%m-%d %H')
                ORDER BY hour ASC
            """, nativeQuery = true)
    List<HourWeatherSummaryProjection> findLast24HoursSummary(
            @Param("stationId") Long stationId,
            @Param("from") Instant from,
            @Param("to") Instant to
    );

    @Query(value = """
            SELECT s.id,s.`name`, s.latitude, s.longitude, ROUND(AVG(w.temperature), 2) AS avg_temperature
            FROM stations s
            JOIN weather_data w ON s.id = w.station_id AND w.record_at >= :from AND w.record_at < :to
            WHERE s.is_public = 1 OR s.id IN (
                SELECT s.id
                FROM stations s
                WHERE s.user_id = :userId
            )
            GROUP BY s.id
            """, nativeQuery = true
    )
    List<StationAvgTemperatureProjection> findAvgTemperatureByTimeRange(
            @Param("userId") Long userId,
            @Param("from") Instant from,
            @Param("to") Instant to
    );

}
