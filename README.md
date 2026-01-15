# Weather Hub

An IoT weather monitoring backend system for collecting, processing, and visualizing real-time environmental data from mini weather stations.

## Introduction

Weather Hub is a backend platform that aggregates real-time data from ESP32-based weather stations via MQTT. It processes sensor readings (temperature, humidity, rainfall, wind speed, dust), checks them against user-defined limits to generate alerts, and broadcasts updates immediately to connected clients using WebSockets. The system supports multiple stations and users, offering REST APIs for historical data and device management.

## Tech Stack

- **Language:** Java 17
- **Framework:** Spring Boot 3.5.7
- **Database:** MySQL (Spring Data JPA)
- **Real-time & IoT:**
  - MQTT (Spring Integration)
  - WebSocket
- **Security:** Spring Security, JWT (JJWT 0.11.5), BCrypt
- **Documentation:** OpenAPI / Swagger (SpringDoc 2.8.14)
- **Tools:** Lombok

## System Architecture

The system is organized using a layered architecture with a unidirectional data flow:

1.  **Ingestion:** An MQTT Adapter subscribes to sensor topics and receives raw data from weather stations.
2.  **Processing:** The service layer parses the payload, validates it, and persists it to the **MySQL** database. Incoming data is simultaneously checked against configured **Thresholds** for the specific station.
3.  **Alerting:** If a threshold is breached, an Alert entity is created and stored.
4.  **Broadcasting:** Event listeners trigger side effects, pushing the new weather data and generated alerts to frontend clients via **WebSocket** for real-time visualization.

### Database Schema

Key entities include:
-   `users`: User and administrator accounts.
-   `roles`: Access control roles (ADMIN, USER).
-   `stations`: Metadata for weather stations.
-   `weather_data`: Time-series sensor readings.
-   `thresholds`: Alert configurations per station.
-   `alerts`: Generated notifications for threshold breaches.
-   `refresh_tokens` / `verification_tokens`: Security and auth tokens.

## Getting Started

### Prerequisites

-   Java 17 Development Kit (JDK 17)
-   Maven 3.11+
-   MySQL Server
-   MQTT Broker (EMQX)

### Installation

1.  Clone the repository:
    ```bash
    git clone <repository_url>
    cd weather-hub
    ```
2.  Build the project:
    ```bash
    mvn clean install
    ```

### Configuration

Create a `src/main/resources/application.properties` file strictly following the template below. Replace the placeholders with your actual environment credentials.

```properties
# Application
spring.application.name=weather-hub
server.port=8080

# Database Configuration (MySQL)
spring.datasource.url=jdbc:mysql://localhost:3306/weather_hub
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update

# MQTT Configuration
mqtt.broker=YOUR_MQTT_BROKER_URL
mqtt.username=YOUR_MQTT_USERNAME
mqtt.password=YOUR_MQTT_PASSWORD
mqtt.clientId=backend-weather-hub
mqtt.topic=weather/+/data

# JWT Security Configuration
jwt.secret=YOUR_JWT_SECRET_KEY
jwt.access-expiration=ACCESS_TOKEN_TTL_IN_MS
jwt.refresh-expiration=REFRESH_TOKEN_TTL_IN_MS

# Mail Configuration (Optional)
spring.mail.host=YOUR_SMTP_HOST
spring.mail.port=YOUR_SMTP_PORT
spring.mail.username=YOUR_EMAIL_USERNAME
spring.mail.password=YOUR_EMAIL_PASSWORD
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### Running the Application

Run the application using Maven:
```bash
mvn spring-boot:run
```

Or run the built JAR file:
```bash
java -jar target/weather-hub-0.0.1-SNAPSHOT.jar
```

## API Documentation

The application exposes a REST API documented via OpenAPI/Swagger.

Once the application is running, access the interactive API documentation at:
`http://localhost:8080/swagger-ui/index.html`

### Main Controller Categories
-   **Auth:** Login, registration, token management.
-   **User:** Profile and account administration.
-   **Station:** Device provisioning and metadata.
-   **Weather:** Historical data retrieval.
-   **Threshold:** Alert rule configuration.
-   **Alert:** Notification management.

## Contributing

This project is maintained for learning and demonstration purposes.
External contributions are not actively accepted.

## License

This project is provided for educational and demonstration purposes only.
No commercial use is permitted
