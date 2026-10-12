package com.petroflow.repository;

import com.petroflow.entity.Fuel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FuelRepository extends JpaRepository<Fuel, Long> {
    Optional<Fuel> findByFuelCode(String fuelCode);
    boolean existsByFuelCode(String fuelCode);
    List<Fuel> findByActiveTrue();
}
