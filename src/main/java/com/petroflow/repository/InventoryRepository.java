package com.petroflow.repository;

import com.petroflow.entity.Fuel;
import com.petroflow.entity.Inventory;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByFuel(Fuel fuel);
    Optional<Inventory> findByFuel_FuelCode(String fuelCode);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM Inventory i WHERE i.fuel = :fuel")
    Optional<Inventory> findByFuelWithLock(@Param("fuel") Fuel fuel);
}
