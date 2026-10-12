package com.petroflow.controller;

import com.petroflow.dto.ApiResponse;
import com.petroflow.dto.EmployeeDto;
import com.petroflow.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EmployeeDto>>> getAllEmployees(@RequestParam(value = "activeOnly", defaultValue = "false") boolean activeOnly) {
        List<EmployeeDto> employees = activeOnly ? employeeService.getActiveEmployees() : employeeService.getAllEmployees();
        return ResponseEntity.ok(ApiResponse.success(employees));
    }

    @GetMapping("/{employeeCode}")
    public ResponseEntity<ApiResponse<EmployeeDto>> getEmployee(@PathVariable String employeeCode) {
        return ResponseEntity.ok(ApiResponse.success(employeeService.getEmployeeByCode(employeeCode)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EmployeeDto>> createEmployee(@Valid @RequestBody EmployeeDto dto) {
        EmployeeDto created = employeeService.createEmployee(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Employee created successfully", created));
    }

    @PutMapping("/{employeeCode}")
    public ResponseEntity<ApiResponse<EmployeeDto>> updateEmployee(@PathVariable String employeeCode, @Valid @RequestBody EmployeeDto dto) {
        EmployeeDto updated = employeeService.updateEmployee(employeeCode, dto);
        return ResponseEntity.ok(ApiResponse.success("Employee updated successfully", updated));
    }
}
