package com.petroflow.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "fuels")
public class Fuel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Fuel code is required")
    @Column(name = "fuel_code", unique = true, nullable = false, length = 30)
    private String fuelCode;

    @NotBlank(message = "Fuel name is required")
    @Column(name = "fuel_name", nullable = false, length = 100)
    private String fuelName;

    @NotNull(message = "Price per unit is required")
    @DecimalMin(value = "0.01", message = "Price per unit must be greater than 0")
    @Column(name = "price_per_unit", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerUnit;

    @NotBlank(message = "Unit is required (L or KG)")
    @Column(name = "unit", nullable = false, length = 10)
    private String unit;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Fuel() {}
    public Fuel(String fuelCode, String fuelName, BigDecimal pricePerUnit, String unit, boolean active) {
        this.fuelCode = fuelCode;
        this.fuelName = fuelName;
        this.pricePerUnit = pricePerUnit;
        this.unit = unit;
        this.active = active;
    }
    @PrePersist protected void onCreate() { this.createdAt = LocalDateTime.now(); this.updatedAt = LocalDateTime.now(); }
    @PreUpdate protected void onUpdate() { this.updatedAt = LocalDateTime.now(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFuelCode() { return fuelCode; }
    public void setFuelCode(String fuelCode) { this.fuelCode = fuelCode; }
    public String getFuelName() { return fuelName; }
    public void setFuelName(String fuelName) { this.fuelName = fuelName; }
    public BigDecimal getPricePerUnit() { return pricePerUnit; }
    public void setPricePerUnit(BigDecimal pricePerUnit) { this.pricePerUnit = pricePerUnit; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
