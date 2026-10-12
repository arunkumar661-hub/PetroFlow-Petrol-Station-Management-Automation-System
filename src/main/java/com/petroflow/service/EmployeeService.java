package com.petroflow.service;

import com.petroflow.dto.EmployeeDto;
import com.petroflow.entity.Employee;
import com.petroflow.exception.ResourceNotFoundException;
import com.petroflow.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final IdentifierGeneratorService identifierGeneratorService;

    public EmployeeService(EmployeeRepository employeeRepository, IdentifierGeneratorService identifierGeneratorService) {
        this.employeeRepository = employeeRepository;
        this.identifierGeneratorService = identifierGeneratorService;
    }

    @Transactional(readOnly = true)
    public List<EmployeeDto> getAllEmployees() {
        return employeeRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EmployeeDto> getActiveEmployees() {
        return employeeRepository.findByStatus("ACTIVE").stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EmployeeDto getEmployeeByCode(String employeeCode) {
        Employee emp = employeeRepository.findByEmployeeCode(employeeCode.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with code: " + employeeCode));
        return mapToDto(emp);
    }

    @Transactional
    public EmployeeDto createEmployee(EmployeeDto dto) {
        String empCode = identifierGeneratorService.generateEmployeeCode();
        LocalDate joiningDate = dto.getJoiningDate() != null ? dto.getJoiningDate() : LocalDate.now();

        Employee emp = new Employee(
                empCode,
                dto.getName().trim(),
                dto.getPhone().trim(),
                dto.getEmail() != null ? dto.getEmail().trim() : null,
                dto.getRole().trim(),
                dto.getSalary(),
                dto.getStatus() != null ? dto.getStatus().toUpperCase() : "ACTIVE",
                joiningDate
        );
        Employee saved = employeeRepository.save(emp);
        return mapToDto(saved);
    }

    @Transactional
    public EmployeeDto updateEmployee(String employeeCode, EmployeeDto dto) {
        Employee emp = employeeRepository.findByEmployeeCode(employeeCode.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with code: " + employeeCode));

        emp.setName(dto.getName().trim());
        emp.setPhone(dto.getPhone().trim());
        emp.setEmail(dto.getEmail() != null ? dto.getEmail().trim() : null);
        emp.setRole(dto.getRole().trim());
        emp.setSalary(dto.getSalary());
        if (dto.getStatus() != null) {
            emp.setStatus(dto.getStatus().toUpperCase());
        }
        if (dto.getJoiningDate() != null) {
            emp.setJoiningDate(dto.getJoiningDate());
        }

        Employee updated = employeeRepository.save(emp);
        return mapToDto(updated);
    }

    private EmployeeDto mapToDto(Employee emp) {
        return new EmployeeDto(
                emp.getEmployeeCode(),
                emp.getName(),
                emp.getPhone(),
                emp.getEmail(),
                emp.getRole(),
                emp.getSalary(),
                emp.getStatus(),
                emp.getJoiningDate()
        );
    }
}
