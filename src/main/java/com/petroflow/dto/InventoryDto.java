package com.petroflow.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InventoryDto {
    private String fuelCode;
    private String fuelName;
    private BigDecimal currentQuantity;
    private String unit;
    private BigDecimal minimumStockLevel;
    private boolean lowStock;
    private LocalDateTime lastUpdated;

    public InventoryDto() {}
    public InventoryDto(String fuelCode, String fuelName, BigDecimal currentQuantity, String unit, BigDecimal minimumStockLevel, boolean lowStock, LocalDateTime lastUpdated) {
        this.fuelCode = fuelCode;
        this.fuelName = fuelName;
        this.currentQuantity = currentQuantity;
        this.unit = unit;
        this.minimumStockLevel = minimumStockLevel;
        this.lowStock = lowStock;
        this.lastUpdated = lastUpdated;
    }

    public String getFuelCode() { return fuelCode; }
    public void setFuelCode(String fuelCode) { this.fuelCode = fuelCode; }
    public String getFuelName() { return fuelName; }
    public void setFuelName(String fuelName) { this.fuelName = fuelName; }
    public BigDecimal getCurrentQuantity() { return currentQuantity; }
    public void setCurrentQuantity(BigDecimal currentQuantity) { this.currentQuantity = currentQuantity; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public BigDecimal getMinimumStockLevel() { return minimumStockLevel; }
    public void setMinimumStockLevel(BigDecimal minimumStockLevel) { this.minimumStockLevel = minimumStockLevel; }
    public boolean isLowStock() { return lowStock; }
    public void setLowStock(boolean lowStock) { this.lowStock = lowStock; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
}
