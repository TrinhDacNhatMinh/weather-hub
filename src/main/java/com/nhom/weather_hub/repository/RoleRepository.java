package com.nhom.weather_hub.repository;

import com.nhom.weather_hub.domain.enums.RoleName;
import com.nhom.weather_hub.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName name);

}
