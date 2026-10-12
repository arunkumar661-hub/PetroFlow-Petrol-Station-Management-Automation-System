package com.petroflow.dto;

public class CustomerDto {
    private String customerCode;
    private String name;
    private String phone;
    private String vehicleNumber;

    public CustomerDto() {}
    public CustomerDto(String customerCode, String name, String phone, String vehicleNumber) {
        this.customerCode = customerCode;
        this.name = name;
        this.phone = phone;
        this.vehicleNumber = vehicleNumber;
    }

    public String getCustomerCode() { return customerCode; }
    public void setCustomerCode(String customerCode) { this.customerCode = customerCode; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }
}
