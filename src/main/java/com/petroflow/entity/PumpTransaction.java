package com.petroflow.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pump_transactions")
public class PumpTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Transaction number is required")
    @Column(name = "transaction_number", unique = true, nullable = false, length = 40)
    private String transactionNumber;

    @NotNull(message = "Pump is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pump_id", nullable = false)
    private Pump pump;

    @NotNull(message = "Fuel is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fuel_id", nullable = false)
    private Fuel fuel;

    @NotNull(message = "Employee is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @NotNull(message = "Quantity is required")
    @DecimalMin(value = "0.01", message = "Quantity must be greater than 0")
    @Column(name = "quantity", nullable = false, precision = 12, scale = 2)
    private BigDecimal quantity;

    @NotBlank(message = "Unit is required")
    @Column(name = "unit", nullable = false, length = 10)
    private String unit;

    @NotNull(message = "Price per unit is required")
    @DecimalMin(value = "0.01", message = "Price per unit must be greater than 0")
    @Column(name = "price_per_unit", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerUnit;

    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.01", message = "Total amount must be greater than 0")
    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @NotBlank(message = "Status is required")
    @Column(name = "status", nullable = false, length = 30)
    private String status = "COMPLETED";

    @NotNull(message = "Transaction date is required")
    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    public PumpTransaction() {}
    public PumpTransaction(String transactionNumber, Pump pump, Fuel fuel, Employee employee,
                           BigDecimal quantity, String unit, BigDecimal pricePerUnit, BigDecimal totalAmount,
                           String status, LocalDateTime transactionDate) {
        this.transactionNumber = transactionNumber;
        this.pump = pump;
        this.fuel = fuel;
        this.employee = employee;
        this.quantity = quantity;
        this.unit = unit;
        this.pricePerUnit = pricePerUnit;
        this.totalAmount = totalAmount;
        this.status = status;
        this.transactionDate = transactionDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTransactionNumber() { return transactionNumber; }
    public void setTransactionNumber(String transactionNumber) { this.transactionNumber = transactionNumber; }
    public Pump getPump() { return pump; }
    public void setPump(Pump pump) { this.pump = pump; }
    public Fuel getFuel() { return fuel; }
    public void setFuel(Fuel fuel) { this.fuel = fuel; }
    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }
    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public BigDecimal getPricePerUnit() { return pricePerUnit; }
    public void setPricePerUnit(BigDecimal pricePerUnit) { this.pricePerUnit = pricePerUnit; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDateTime transactionDate) { this.transactionDate = transactionDate; }
}
