package com.petroflow.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class EmployeeDto {
    private String employeeCode;

    @NotBlank(message = "Employee name is required")
    private String name;

    @NotBlank(message = "Phone number is required")
    private String phone;

    @Email(message = "Valid email is required")
    private String email;

    @NotBlank(message = "Role is required")
    private String role;

    @NotNull(message = "Salary is required")
    @DecimalMin(value = "0.00", message = "Salary must be positive")
    private BigDecimal salary;

    private String status = "ACTIVE";
    private LocalDate joiningDate;

    public EmployeeDto() {}
    public EmployeeDto(String employeeCode, String name, String phone, String email, String role, BigDecimal salary, String status, LocalDate joiningDate) {
        this.employeeCode = employeeCode;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.role = role;
        this.salary = salary;
        this.status = status;
        this.joiningDate = joiningDate;
    }

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
}
