package com.petroflow.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PumpTransactionDto {
    private String transactionNumber;
    private String pumpCode;
    private String pumpName;
    private String fuelCode;
    private String fuelName;
    private String employeeCode;
    private String employeeName;
    private BigDecimal quantity;
    private String unit;
    private BigDecimal pricePerUnit;
    private BigDecimal totalAmount;
    private String status;
    private LocalDateTime transactionDate;

    public PumpTransactionDto() {}

    public String getTransactionNumber() { return transactionNumber; }
    public void setTransactionNumber(String transactionNumber) { this.transactionNumber = transactionNumber; }
    public String getPumpCode() { return pumpCode; }
    public void setPumpCode(String pumpCode) { this.pumpCode = pumpCode; }
    public String getPumpName() { return pumpName; }
    public void setPumpName(String pumpName) { this.pumpName = pumpName; }
    public String getFuelCode() { return fuelCode; }
    public void setFuelCode(String fuelCode) { this.fuelCode = fuelCode; }
    public String getFuelName() { return fuelName; }
    public void setFuelName(String fuelName) { this.fuelName = fuelName; }
    public String getEmployeeCode() { return employeeCode; }
    public void setEmployeeCode(String employeeCode) { this.employeeCode = employeeCode; }
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
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
