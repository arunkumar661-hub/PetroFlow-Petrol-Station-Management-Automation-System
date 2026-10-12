package com.petroflow.dto;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class PumpDto {
    private String pumpCode;

    @NotBlank(message = "Pump name is required")
    private String pumpName;

    private String status = "ONLINE";

    @NotBlank(message = "Supported fuel code is required")
    private String supportedFuelCode;
    private String supportedFuelName;
    private BigDecimal fuelPrice;
    private String fuelUnit;
    private String lastTransactionNumber;
    private BigDecimal lastDispensedQuantity;

    public PumpDto() {}
    public PumpDto(String pumpCode, String pumpName, String status, String supportedFuelCode, String supportedFuelName, BigDecimal fuelPrice, String fuelUnit) {
        this.pumpCode = pumpCode;
        this.pumpName = pumpName;
        this.status = status;
        this.supportedFuelCode = supportedFuelCode;
        this.supportedFuelName = supportedFuelName;
        this.fuelPrice = fuelPrice;
        this.fuelUnit = fuelUnit;
    }

    public String getPumpCode() { return pumpCode; }
    public void setPumpCode(String pumpCode) { this.pumpCode = pumpCode; }
    public String getPumpName() { return pumpName; }
    public void setPumpName(String pumpName) { this.pumpName = pumpName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getSupportedFuelCode() { return supportedFuelCode; }
    public void setSupportedFuelCode(String supportedFuelCode) { this.supportedFuelCode = supportedFuelCode; }
    public String getSupportedFuelName() { return supportedFuelName; }
    public void setSupportedFuelName(String supportedFuelName) { this.supportedFuelName = supportedFuelName; }
    public BigDecimal getFuelPrice() { return fuelPrice; }
    public void setFuelPrice(BigDecimal fuelPrice) { this.fuelPrice = fuelPrice; }
    public String getFuelUnit() { return fuelUnit; }
    public void setFuelUnit(String fuelUnit) { this.fuelUnit = fuelUnit; }
    public String getLastTransactionNumber() { return lastTransactionNumber; }
    public void setLastTransactionNumber(String lastTransactionNumber) { this.lastTransactionNumber = lastTransactionNumber; }
    public BigDecimal getLastDispensedQuantity() { return lastDispensedQuantity; }
    public void setLastDispensedQuantity(BigDecimal lastDispensedQuantity) { this.lastDispensedQuantity = lastDispensedQuantity; }
}
