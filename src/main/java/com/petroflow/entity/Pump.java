package com.petroflow.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "pumps")
public class Pump {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Pump code is required")
    @Column(name = "pump_code", unique = true, nullable = false, length = 30)
    private String pumpCode;

    @NotBlank(message = "Pump name is required")
    @Column(name = "pump_name", nullable = false, length = 100)
    private String pumpName;

    @NotBlank(message = "Pump status is required")
    @Column(name = "status", nullable = false, length = 30)
    private String status = "ONLINE"; // ONLINE, OFFLINE, MAINTENANCE

    @NotNull(message = "Supported fuel is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "supported_fuel_id", nullable = false)
    private Fuel supportedFuel;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Pump() {}
    public Pump(String pumpCode, String pumpName, String status, Fuel supportedFuel) {
        this.pumpCode = pumpCode;
        this.pumpName = pumpName;
        this.status = status;
        this.supportedFuel = supportedFuel;
    }
    @PrePersist protected void onCreate() { this.createdAt = LocalDateTime.now(); this.updatedAt = LocalDateTime.now(); }
    @PreUpdate protected void onUpdate() { this.updatedAt = LocalDateTime.now(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPumpCode() { return pumpCode; }
    public void setPumpCode(String pumpCode) { this.pumpCode = pumpCode; }
    public String getPumpName() { return pumpName; }
    public void setPumpName(String pumpName) { this.pumpName = pumpName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Fuel getSupportedFuel() { return supportedFuel; }
    public void setSupportedFuel(Fuel supportedFuel) { this.supportedFuel = supportedFuel; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
