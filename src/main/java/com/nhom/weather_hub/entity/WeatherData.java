package com.nhom.weather_hub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "weather_data", indexes = {
        @Index(name = "idx_weather_data_record_at", columnList = "record_at"),
        @Index(name = "idx_weather_data_station_id", columnList = "station_id")
})
public class WeatherData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "temperature")
    private Float temperature;

    @Column(name = "humidity")
    @Max(100)
    @Min(0)
    private Float humidity;

    @Column(name = "wind_speed")
    @Min(0)
    private Float windSpeed;

    @Column(name = "rainfall")
    @Min(0)
    private Float rainfall;

    @Column(name = "dust")
    @Min(0)
    private Float dust;

    @Column(name = "record_at", nullable = false)
    private Instant recordAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    @OneToOne(mappedBy = "weatherData", cascade = CascadeType.ALL, orphanRemoval = true)
    private Alert alert;

}
