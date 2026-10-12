package com.petroflow.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class SupplierDto {
    private String supplierCode;

    @NotBlank(message = "Supplier name is required")
    private String name;

    @NotBlank(message = "Phone number is required")
    private String phone;

    @Email(message = "Valid email is required")
    private String email;

    private String address;
    private String status = "ACTIVE";

    public SupplierDto() {}
    public SupplierDto(String supplierCode, String name, String phone, String email, String address, String status) {
        this.supplierCode = supplierCode;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.status = status;
    }

    public String getSupplierCode() { return supplierCode; }
    public void setSupplierCode(String supplierCode) { this.supplierCode = supplierCode; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
