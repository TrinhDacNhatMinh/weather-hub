package com.nhom.weather_hub.repository;

import com.nhom.weather_hub.entity.WeatherData;
import com.nhom.weather_hub.projection.CurrentWeatherDataProjection;
import com.nhom.weather_hub.projection.DailyWeatherSummaryProjection;
import com.nhom.weather_hub.projection.HourWeatherSummaryProjection;
import com.nhom.weather_hub.projection.StationAvgTemperatureProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query(value = """
            SELECT 
                current.id,
                current.temperature,
                current.humidity,
                wind_10m.avg_wind_speed AS windSpeed,
                rain_1h.total_rainfall AS rainfall,
                dust_1h.avg_dust AS dust,
                current.record_at AS recordAt,
                s.id AS stationId,
                s.name AS stationName,
                s.updated_at AS updatedAt,
                s.latitude,
                s.longitude
            FROM stations s
            LEFT JOIN (
                SELECT w1.id, w1.station_id, w1.temperature, w1.humidity, w1.record_at
                FROM weather_data w1
                INNER JOIN (
                    SELECT station_id, MAX(record_at) AS max_record_at
                    FROM weather_data
                    WHERE record_at >= DATE_SUB(NOW(), INTERVAL 1 MINUTE)
                    GROUP BY station_id
                ) max_w ON w1.station_id = max_w.station_id AND w1.record_at = max_w.max_record_at
            ) current ON s.id = current.station_id
            LEFT JOIN (
                SELECT station_id, ROUND(AVG(wind_speed), 2) AS avg_wind_speed
                FROM weather_data
                WHERE record_at >= DATE_SUB(NOW(), INTERVAL 10 MINUTE)
                GROUP BY station_id
            ) wind_10m ON s.id = wind_10m.station_id
            LEFT JOIN (
                SELECT station_id, ROUND(SUM(rainfall) / 3600, 2) AS total_rainfall
                FROM weather_data
                WHERE record_at >= DATE_SUB(NOW(), INTERVAL 1 HOUR)
                GROUP BY station_id
            ) rain_1h ON s.id = rain_1h.station_id
            LEFT JOIN (
                SELECT station_id, ROUND(AVG(dust), 2) AS avg_dust
                FROM weather_data
                WHERE record_at >= DATE_SUB(NOW(), INTERVAL 1 HOUR)
                GROUP BY station_id
            ) dust_1h ON s.id = dust_1h.station_id
            WHERE s.is_public = 1 OR s.user_id = :userId
            ORDER BY current.record_at IS NULL, current.record_at DESC, s.name ASC
            """, 
            countQuery = """
            SELECT COUNT(s.id)
            FROM stations s
            WHERE s.is_public = 1 OR s.user_id = :userId
            """,
            nativeQuery = true)
    Page<CurrentWeatherDataProjection> findCurrentWeatherDataForUserAndPublicStations(
            @Param("userId") Long userId,
            Pageable pageable
    );
    
    void deleteByStationId(Long stationId);

}
