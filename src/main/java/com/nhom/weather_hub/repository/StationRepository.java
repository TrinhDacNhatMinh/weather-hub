package com.nhom.weather_hub.repository;

import com.nhom.weather_hub.entity.Station;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StationRepository extends JpaRepository<Station, Long> {

    Optional<Station> findByApiKey(String apiKey);

    Page<Station> findByUserId(Long userId, Pageable pageable);

    Page<Station> findByIsPublicTrue(Pageable pageable);

    boolean existsByApiKey(String apiKey);

}
