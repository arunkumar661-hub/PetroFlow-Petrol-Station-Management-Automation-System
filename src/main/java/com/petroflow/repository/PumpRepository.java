package com.petroflow.repository;

import com.petroflow.entity.Pump;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PumpRepository extends JpaRepository<Pump, Long> {
    Optional<Pump> findByPumpCode(String pumpCode);
    boolean existsByPumpCode(String pumpCode);
    List<Pump> findByStatus(String status);
    long count();
}
