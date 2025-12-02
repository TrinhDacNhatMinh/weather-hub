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
        @Index(name = "idx_record_at", columnList = "record_at")
})
public class WeatherData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "temperature")
    private Double temperature;

    @Column(name = "humidity")
    @Max(100)
    @Min(0)
    private Double humidity;

    @Column(name = "wind_speed")
    @Min(0)
    private Double windSpeed;

    @Column(name = "rainfall")
    @Min(0)
    private Double rainfall;

    @Column(name = "dust")
    @Min(0)
    private Double dust;

    @Column(name = "record_at", nullable = false)
    private Instant recordAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

}
