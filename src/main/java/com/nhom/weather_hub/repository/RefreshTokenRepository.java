package com.nhom.weather_hub.repository;

import com.nhom.weather_hub.entity.RefreshToken;
import com.nhom.weather_hub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    public Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user);

}
