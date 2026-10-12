package com.petroflow.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Employee code is required")
    @Column(name = "employee_code", unique = true, nullable = false, length = 30)
    private String employeeCode;

    @NotBlank(message = "Employee name is required")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Phone number is required")
    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Email(message = "Valid email is required")
    @Column(name = "email", length = 100)
    private String email;

    @NotBlank(message = "Role is required")
    @Column(name = "role", nullable = false, length = 50)
    private String role;

    @NotNull(message = "Salary is required")
    @DecimalMin(value = "0.00", message = "Salary must be positive")
    @Column(name = "salary", nullable = false, precision = 10, scale = 2)
    private BigDecimal salary;

    @NotBlank(message = "Status is required")
    @Column(name = "status", nullable = false, length = 20)
    private String status = "ACTIVE";

    @NotNull(message = "Joining date is required")
    @Column(name = "joining_date", nullable = false)
    private LocalDate joiningDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Employee() {}
    public Employee(String employeeCode, String name, String phone, String email, String role, BigDecimal salary, String status, LocalDate joiningDate) {
        this.employeeCode = employeeCode;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.role = role;
        this.salary = salary;
        this.status = status;
        this.joiningDate = joiningDate;
    }
    @PrePersist protected void onCreate() { this.createdAt = LocalDateTime.now(); this.updatedAt = LocalDateTime.now(); }
    @PreUpdate protected void onUpdate() { this.updatedAt = LocalDateTime.now(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmployeeCode() { return employeeCode; }
    public void setEmployeeCode(String employeeCode) { this.employeeCode = employeeCode; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public BigDecimal getSalary() { return salary; }
    public void setSalary(BigDecimal salary) { this.salary = salary; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getJoiningDate() { return joiningDate; }
    public void setJoiningDate(LocalDate joiningDate) { this.joiningDate = joiningDate; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
