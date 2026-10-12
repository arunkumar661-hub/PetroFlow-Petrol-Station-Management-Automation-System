package com.petroflow.repository;

import com.petroflow.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
    Optional<Invoice> findByTransactionNumber(String transactionNumber);
    List<Invoice> findAllByOrderByInvoiceDateDesc();
    List<Invoice> findByInvoiceDateBetweenOrderByInvoiceDateDesc(LocalDateTime start, LocalDateTime end);
    long count();
}
