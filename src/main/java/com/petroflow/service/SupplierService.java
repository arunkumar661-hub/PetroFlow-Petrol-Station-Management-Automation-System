package com.petroflow.service;

import com.petroflow.dto.SupplierDto;
import com.petroflow.entity.Supplier;
import com.petroflow.exception.ResourceNotFoundException;
import com.petroflow.repository.SupplierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final IdentifierGeneratorService identifierGeneratorService;

    public SupplierService(SupplierRepository supplierRepository, IdentifierGeneratorService identifierGeneratorService) {
        this.supplierRepository = supplierRepository;
        this.identifierGeneratorService = identifierGeneratorService;
    }

    @Transactional(readOnly = true)
    public List<SupplierDto> getAllSuppliers() {
        return supplierRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SupplierDto getSupplierByCode(String supplierCode) {
        Supplier sup = supplierRepository.findBySupplierCode(supplierCode.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with code: " + supplierCode));
        return mapToDto(sup);
    }

    @Transactional
    public SupplierDto createSupplier(SupplierDto dto) {
        String supCode = identifierGeneratorService.generateSupplierCode();
        Supplier sup = new Supplier(
                supCode,
                dto.getName().trim(),
                dto.getPhone().trim(),
                dto.getEmail() != null ? dto.getEmail().trim() : null,
                dto.getAddress(),
                dto.getStatus() != null ? dto.getStatus().toUpperCase() : "ACTIVE"
        );
        Supplier saved = supplierRepository.save(sup);
        return mapToDto(saved);
    }

    @Transactional
    public SupplierDto updateSupplier(String supplierCode, SupplierDto dto) {
        Supplier sup = supplierRepository.findBySupplierCode(supplierCode.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with code: " + supplierCode));

        sup.setName(dto.getName().trim());
        sup.setPhone(dto.getPhone().trim());
        sup.setEmail(dto.getEmail() != null ? dto.getEmail().trim() : null);
        sup.setAddress(dto.getAddress());
        if (dto.getStatus() != null) {
            sup.setStatus(dto.getStatus().toUpperCase());
        }
        Supplier updated = supplierRepository.save(sup);
        return mapToDto(updated);
    }

    private SupplierDto mapToDto(Supplier sup) {
        return new SupplierDto(
                sup.getSupplierCode(),
                sup.getName(),
                sup.getPhone(),
                sup.getEmail(),
                sup.getAddress(),
                sup.getStatus()
        );
    }
}
