package com.example.gorido.Repository;
import com.example.gorido.Model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Integer> {
    Optional<Driver> findByUserId(User user);
}
