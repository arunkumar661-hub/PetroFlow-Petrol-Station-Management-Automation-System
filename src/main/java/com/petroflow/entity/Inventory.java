package com.petroflow.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventories")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Fuel reference is required")
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fuel_id", unique = true, nullable = false)
    private Fuel fuel;

    @NotNull(message = "Current quantity is required")
    @DecimalMin(value = "0.00", message = "Current quantity cannot be negative")
    @Column(name = "current_quantity", nullable = false, precision = 12, scale = 2)
    private BigDecimal currentQuantity;

    @NotNull(message = "Minimum stock level is required")
    @DecimalMin(value = "0.00", message = "Minimum stock level cannot be negative")
    @Column(name = "minimum_stock_level", nullable = false, precision = 12, scale = 2)
    private BigDecimal minimumStockLevel;

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;

    @Version
    @Column(name = "version")
    private Long version;

    public Inventory() {}
    public Inventory(Fuel fuel, BigDecimal currentQuantity, BigDecimal minimumStockLevel) {
        this.fuel = fuel;
        this.currentQuantity = currentQuantity;
        this.minimumStockLevel = minimumStockLevel;
    }
    @PrePersist @PreUpdate protected void onSave() { this.lastUpdated = LocalDateTime.now(); }

    public boolean isLowStock() {
        if (currentQuantity == null || minimumStockLevel == null) return false;
        return currentQuantity.compareTo(minimumStockLevel) <= 0;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Fuel getFuel() { return fuel; }
    public void setFuel(Fuel fuel) { this.fuel = fuel; }
    public BigDecimal getCurrentQuantity() { return currentQuantity; }
    public void setCurrentQuantity(BigDecimal currentQuantity) { this.currentQuantity = currentQuantity; }
    public BigDecimal getMinimumStockLevel() { return minimumStockLevel; }
    public void setMinimumStockLevel(BigDecimal minimumStockLevel) { this.minimumStockLevel = minimumStockLevel; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
}
