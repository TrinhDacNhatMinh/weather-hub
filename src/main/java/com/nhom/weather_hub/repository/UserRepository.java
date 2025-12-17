package com.nhom.weather_hub.repository;

import com.nhom.weather_hub.entity.User;
import com.nhom.weather_hub.projection.UserWithStationCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query(
            """
                    SELECT
                        u.id AS id,
                        u.name AS name,
                        u.username AS username,
                        u.email AS email,
                        u.active AS active,
                        COUNT(s.id) AS stationCount
                    FROM User u LEFT JOIN Station s ON s.user.id = u.id
                    GROUP BY u.id
                    """
    )
    Page<UserWithStationCount> findUserWithStationCount(Pageable pageable);

}
