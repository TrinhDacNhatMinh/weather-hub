package com.nhom.weather_hub.repository;

import com.nhom.weather_hub.entity.Station;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StationRepository extends JpaRepository<Station, Long> {

    public Optional<Station> findByApiKey(String apiKey);

    public Page<Station> findByUserId(Long userId, Pageable pageable);

    public boolean existsByApiKey(String apiKey);

}
