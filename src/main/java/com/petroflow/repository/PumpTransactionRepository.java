package com.petroflow.repository;

import com.petroflow.entity.PumpTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PumpTransactionRepository extends JpaRepository<PumpTransaction, Long> {
    Optional<PumpTransaction> findByTransactionNumber(String transactionNumber);
    List<PumpTransaction> findAllByOrderByTransactionDateDesc();
    List<PumpTransaction> findByTransactionDateBetweenOrderByTransactionDateDesc(LocalDateTime start, LocalDateTime end);
    Optional<PumpTransaction> findTopByPump_PumpCodeOrderByTransactionDateDesc(String pumpCode);
    long count();
}
