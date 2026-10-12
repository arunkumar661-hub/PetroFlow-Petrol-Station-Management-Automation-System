package com.petroflow.controller;

import com.petroflow.dto.ApiResponse;
import com.petroflow.dto.SupplierDto;
import com.petroflow.service.SupplierService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SupplierDto>>> getAllSuppliers() {
        return ResponseEntity.ok(ApiResponse.success(supplierService.getAllSuppliers()));
    }

    @GetMapping("/{supplierCode}")
    public ResponseEntity<ApiResponse<SupplierDto>> getSupplier(@PathVariable String supplierCode) {
        return ResponseEntity.ok(ApiResponse.success(supplierService.getSupplierByCode(supplierCode)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SupplierDto>> createSupplier(@Valid @RequestBody SupplierDto dto) {
        SupplierDto created = supplierService.createSupplier(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Supplier registered successfully", created));
    }

    @PutMapping("/{supplierCode}")
    public ResponseEntity<ApiResponse<SupplierDto>> updateSupplier(@PathVariable String supplierCode, @Valid @RequestBody SupplierDto dto) {
        SupplierDto updated = supplierService.updateSupplier(supplierCode, dto);
        return ResponseEntity.ok(ApiResponse.success("Supplier updated successfully", updated));
    }
}
