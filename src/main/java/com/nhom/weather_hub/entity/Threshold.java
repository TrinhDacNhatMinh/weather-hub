package com.nhom.weather_hub.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "thresholds",
        indexes = {
                @Index(name = "idx_threshold_station", columnList = "station_id")
        }
)
public class Threshold {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "temperature_min", nullable = false)
    private Float temperatureMin;

    @Column(name = "temperature_max", nullable = false)
    private Float temperatureMax;

    @Column(name = "humidity_min", nullable = false)
    private Float humidityMin;

    @Column(name = "humidity_max", nullable = false)
    private Float humidityMax;

    @Column(name = "rainfall_max", nullable = false)
    private Float rainfallMax;

    @Column(name = "wind_speed_max", nullable = false)
    private Float windSpeedMax;

    @Column(name = "dust_max", nullable = false)
    private Float dustMax;

    @Column(name = "temperature_active", nullable = false)
    private Boolean temperatureActive;

    @Column(name = "humidity_active", nullable = false)
    private Boolean humidityActive;

    @Column(name = "rainfall_active", nullable = false)
    private Boolean rainfallActive;

    @Column(name = "wind_speed_active", nullable = false)
    private Boolean windSpeedActive;

    @Column(name = "dust_active", nullable = false)
    private Boolean dustActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id", nullable = false, unique = true)
    private Station station;

}
