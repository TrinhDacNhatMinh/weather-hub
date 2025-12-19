package com.nhom.weather_hub;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
		info = @Info(
				title = "Weather Hub API",
				version = "2.0",
				description = "Comprehensive API documentation for the Weather Hub application."
		)
)
@SpringBootApplication
public class WeatherHubApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherHubApplication.class, args);
	}

}
