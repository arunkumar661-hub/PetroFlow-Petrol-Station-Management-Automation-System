package com.petroflow.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class StockAdjustmentDto {
    @NotBlank(message = "Fuel code is required")
    private String fuelCode;

    @NotNull(message = "Quantity is required")
    @DecimalMin(value = "0.01", message = "Quantity must be greater than 0")
    private BigDecimal quantity;

    @NotBlank(message = "Action type is required (ADD or SET)")
    private String action = "ADD"; // ADD or SET

    private String notes;

    public StockAdjustmentDto() {}
    public String getFuelCode() { return fuelCode; }
    public void setFuelCode(String fuelCode) { this.fuelCode = fuelCode; }
    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
