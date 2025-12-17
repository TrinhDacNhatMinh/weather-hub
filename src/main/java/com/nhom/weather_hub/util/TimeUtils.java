package com.nhom.weather_hub.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class TimeUtils {

    public static Instant nowVn() {
        return  Instant.now().plus(7, ChronoUnit.HOURS);
    }

}
