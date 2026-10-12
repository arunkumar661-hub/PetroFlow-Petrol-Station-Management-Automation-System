package com.petroflow.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class FuelDto {
    private String fuelCode;

    @NotBlank(message = "Fuel name is required")
    private String fuelName;

    @NotNull(message = "Price per unit is required")
    @DecimalMin(value = "0.01", message = "Price per unit must be greater than 0")
    private BigDecimal pricePerUnit;

    @NotBlank(message = "Unit is required (L or KG)")
    private String unit;

    private boolean active = true;

    public FuelDto() {}
    public FuelDto(String fuelCode, String fuelName, BigDecimal pricePerUnit, String unit, boolean active) {
        this.fuelCode = fuelCode;
        this.fuelName = fuelName;
        this.pricePerUnit = pricePerUnit;
        this.unit = unit;
        this.active = active;
    }

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
}
