package com.nhom.weather_hub.projection;

import java.math.BigDecimal;
import java.time.Instant;

public interface CurrentWeatherDataProjection {
    Long getId();
    Float getTemperature();
    Float getHumidity();
    Float getWindSpeed();
    Float getRainfall();
    Float getDust();
    Instant getRecordAt();
    Long getStationId();
    String getStationName();
    Instant getUpdatedAt();
    BigDecimal getLatitude();
    BigDecimal getLongitude();
}
