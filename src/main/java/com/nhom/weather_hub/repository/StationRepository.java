package com.nhom.weather_hub.repository;

import com.nhom.weather_hub.entity.Station;
import com.nhom.weather_hub.projection.StationMapProjection;
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
public interface StationRepository extends JpaRepository<Station, Long> {

    Optional<Station> findByApiKey(String apiKey);

    Page<Station> findByUserId(Long userId, Pageable pageable);

    Page<Station> findByIsPublicTrue(Pageable pageable);

    boolean existsByApiKey(String apiKey);

    @Query(value = """
            SELECT 	    s.id AS stationId,
            			s.`name` AS stationName,
            			s.latitude AS latitude,
            			s.longitude AS longitude,
            			ROUND(AVG(w.temperature), 2) AS temperature,
            			ROUND(AVG(w.humidity), 2) AS humidity,
            			ROUND(AVG(w.wind_speed), 2) AS windSpeed,
            			ROUND(SUM(w.rainfall / 3600), 2) AS totalRainfall,
            			ROUND(AVG(w.dust), 2) AS dust
            FROM stations s LEFT JOIN weather_data w ON s.id = w.station_id AND w.record_at >= :from AND w.record_at < :to
            WHERE (s.is_public = 1 AND :includePublic = TRUE) OR s.id IN (
            	SELECT s.id
            	FROM stations s
            	WHERE s.user_id = :userId
            )
            GROUP BY s.id
            """, nativeQuery = true)
    List<StationMapProjection> findStationsForMap(
            @Param("userId") Long userId,
            @Param("from") Instant from,
            @Param("to") Instant to,
            @Param("includePublic") boolean includePublic
    );

}
