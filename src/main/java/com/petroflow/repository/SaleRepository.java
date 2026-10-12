package com.petroflow.repository;

import com.petroflow.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    Optional<Sale> findByTransactionNumber(String transactionNumber);
    List<Sale> findAllByOrderBySaleDateDesc();
    List<Sale> findBySaleDateBetweenOrderBySaleDateDesc(LocalDateTime start, LocalDateTime end);

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.saleDate >= :startOfDay AND s.saleDate <= :endOfDay")
    long countTodaySales(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    @Query("SELECT COALESCE(SUM(s.totalAmount), 0) FROM Sale s WHERE s.saleDate >= :startOfDay AND s.saleDate <= :endOfDay")
    BigDecimal sumTodayRevenue(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    @Query("SELECT COALESCE(SUM(s.quantity), 0) FROM Sale s WHERE s.saleDate >= :startOfDay AND s.saleDate <= :endOfDay")
    BigDecimal sumTodayFuelDispensed(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    long count();
}
