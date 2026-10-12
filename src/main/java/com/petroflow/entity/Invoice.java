package com.petroflow.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Invoice number is required")
    @Column(name = "invoice_number", unique = true, nullable = false, length = 40)
    private String invoiceNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id", unique = true, nullable = false)
    private Sale sale;

    @NotBlank(message = "Transaction number is required")
    @Column(name = "transaction_number", nullable = false, length = 40)
    private String transactionNumber;

    @Column(name = "customer_name", length = 100)
    private String customerName = "Walk-in Customer";

    @Column(name = "vehicle_number", length = 30)
    private String vehicleNumber;

    @Column(name = "pump_code", nullable = false, length = 30)
    private String pumpCode;

    @Column(name = "fuel_name", nullable = false, length = 100)
    private String fuelName;

    @NotNull(message = "Quantity is required")
    @DecimalMin(value = "0.01")
    @Column(name = "quantity", nullable = false, precision = 12, scale = 2)
    private BigDecimal quantity;

    @Column(name = "unit", nullable = false, length = 10)
    private String unit;

    @NotNull(message = "Price per unit is required")
    @Column(name = "price_per_unit", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerUnit;

    @NotNull(message = "Subtotal is required")
    @Column(name = "subtotal", nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "tax_rate", precision = 5, scale = 2)
    private BigDecimal taxRate = BigDecimal.ZERO;

    @Column(name = "tax_amount", precision = 12, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @NotNull(message = "Total amount is required")
    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "payment_method", length = 30)
    private String paymentMethod = "CASH";

    @NotNull(message = "Invoice date is required")
    @Column(name = "invoice_date", nullable = false)
    private LocalDateTime invoiceDate;

    public Invoice() {}
    public Invoice(String invoiceNumber, Sale sale, String transactionNumber, String customerName,
                   String vehicleNumber, String pumpCode, String fuelName, BigDecimal quantity,
                   String unit, BigDecimal pricePerUnit, BigDecimal subtotal, BigDecimal taxRate,
                   BigDecimal taxAmount, BigDecimal totalAmount, String paymentMethod, LocalDateTime invoiceDate) {
        this.invoiceNumber = invoiceNumber;
        this.sale = sale;
        this.transactionNumber = transactionNumber;
        this.customerName = (customerName != null && !customerName.isBlank()) ? customerName : "Walk-in Customer";
        this.vehicleNumber = vehicleNumber;
        this.pumpCode = pumpCode;
        this.fuelName = fuelName;
        this.quantity = quantity;
        this.unit = unit;
        this.pricePerUnit = pricePerUnit;
        this.subtotal = subtotal;
        this.taxRate = taxRate != null ? taxRate : BigDecimal.ZERO;
        this.taxAmount = taxAmount != null ? taxAmount : BigDecimal.ZERO;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod != null ? paymentMethod : "CASH";
        this.invoiceDate = invoiceDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }
    public Sale getSale() { return sale; }
    public void setSale(Sale sale) { this.sale = sale; }
    public String getTransactionNumber() { return transactionNumber; }
    public void setTransactionNumber(String transactionNumber) { this.transactionNumber = transactionNumber; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }
    public String getPumpCode() { return pumpCode; }
    public void setPumpCode(String pumpCode) { this.pumpCode = pumpCode; }
    public String getFuelName() { return fuelName; }
    public void setFuelName(String fuelName) { this.fuelName = fuelName; }
    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public BigDecimal getPricePerUnit() { return pricePerUnit; }
    public void setPricePerUnit(BigDecimal pricePerUnit) { this.pricePerUnit = pricePerUnit; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public BigDecimal getTaxRate() { return taxRate; }
    public void setTaxRate(BigDecimal taxRate) { this.taxRate = taxRate; }
    public BigDecimal getTaxAmount() { return taxAmount; }
    public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public LocalDateTime getInvoiceDate() { return invoiceDate; }
    public void setInvoiceDate(LocalDateTime invoiceDate) { this.invoiceDate = invoiceDate; }
}
