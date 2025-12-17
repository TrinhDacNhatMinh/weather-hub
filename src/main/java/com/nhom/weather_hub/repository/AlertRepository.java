package com.nhom.weather_hub.repository;

import com.nhom.weather_hub.entity.Alert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {

    Page<Alert> findByWeatherData_Station_User_Id(Long userId, Pageable pageable);

    void deleteAllByWeatherData_Station_User_Id(Long userId);

}
