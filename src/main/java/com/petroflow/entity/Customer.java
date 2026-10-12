package com.petroflow.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_code", unique = true, nullable = false, length = 30)
    private String customerCode;

    @Column(name = "name", nullable = false, length = 100)
    private String name = "Walk-in Customer";

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "vehicle_number", length = 30)
    private String vehicleNumber;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Customer() {}
    public Customer(String customerCode, String name, String phone, String vehicleNumber) {
        this.customerCode = customerCode;
        this.name = (name != null && !name.isBlank()) ? name : "Walk-in Customer";
        this.phone = phone;
        this.vehicleNumber = vehicleNumber;
    }
    @PrePersist protected void onCreate() { this.createdAt = LocalDateTime.now(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCustomerCode() { return customerCode; }
    public void setCustomerCode(String customerCode) { this.customerCode = customerCode; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
