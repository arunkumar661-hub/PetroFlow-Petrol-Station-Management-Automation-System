package com.petroflow.controller;

import com.petroflow.dto.ApiResponse;
import com.petroflow.dto.DispenseRequestDto;
import com.petroflow.dto.InvoiceDto;
import com.petroflow.dto.PumpTransactionDto;
import com.petroflow.entity.PumpTransaction;
import com.petroflow.repository.PumpTransactionRepository;
import com.petroflow.service.PumpDispenserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pump-transactions")
public class PumpTransactionController {

    private final PumpDispenserService pumpDispenserService;
    private final PumpTransactionRepository pumpTransactionRepository;

    public PumpTransactionController(PumpDispenserService pumpDispenserService,
                                     PumpTransactionRepository pumpTransactionRepository) {
        this.pumpDispenserService = pumpDispenserService;
        this.pumpTransactionRepository = pumpTransactionRepository;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PumpTransactionDto>>> getAllTransactions() {
        List<PumpTransactionDto> list = pumpTransactionRepository.findAllByOrderByTransactionDateDesc()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @PostMapping("/dispense")
    public ResponseEntity<ApiResponse<InvoiceDto>> processDispense(@Valid @RequestBody DispenseRequestDto request) {
        InvoiceDto invoice = pumpDispenserService.processDispenseTransaction(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Fuel transaction completed successfully. Invoice generated.", invoice));
    }

    private PumpTransactionDto mapToDto(PumpTransaction pt) {
        PumpTransactionDto dto = new PumpTransactionDto();
        dto.setTransactionNumber(pt.getTransactionNumber());
        dto.setPumpCode(pt.getPump().getPumpCode());
        dto.setPumpName(pt.getPump().getPumpName());
        dto.setFuelCode(pt.getFuel().getFuelCode());
        dto.setFuelName(pt.getFuel().getFuelName());
        dto.setEmployeeCode(pt.getEmployee().getEmployeeCode());
        dto.setEmployeeName(pt.getEmployee().getName());
        dto.setQuantity(pt.getQuantity());
        dto.setUnit(pt.getUnit());
        dto.setPricePerUnit(pt.getPricePerUnit());
        dto.setTotalAmount(pt.getTotalAmount());
        dto.setStatus(pt.getStatus());
        dto.setTransactionDate(pt.getTransactionDate());
        return dto;
    }
}
