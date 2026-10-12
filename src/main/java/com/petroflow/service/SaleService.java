package com.petroflow.service;

import com.petroflow.dto.SaleDto;
import com.petroflow.entity.Sale;
import com.petroflow.exception.ResourceNotFoundException;
import com.petroflow.repository.SaleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleService {

    private final SaleRepository saleRepository;

    public SaleService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    @Transactional(readOnly = true)
    public List<SaleDto> getAllSales() {
        return saleRepository.findAllByOrderBySaleDateDesc().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SaleDto getSaleByTransactionNumber(String transactionNumber) {
        Sale sale = saleRepository.findByTransactionNumber(transactionNumber.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found for transaction: " + transactionNumber));
        return mapToDto(sale);
    }

    public SaleDto mapToDto(Sale s) {
        SaleDto dto = new SaleDto();
        dto.setTransactionNumber(s.getTransactionNumber());
        dto.setPumpCode(s.getPump().getPumpCode());
        dto.setPumpName(s.getPump().getPumpName());
        dto.setFuelCode(s.getFuel().getFuelCode());
        dto.setFuelName(s.getFuel().getFuelName());
        dto.setEmployeeCode(s.getEmployee().getEmployeeCode());
        dto.setEmployeeName(s.getEmployee().getName());
        dto.setQuantity(s.getQuantity());
        dto.setUnit(s.getUnit());
        dto.setPricePerUnit(s.getPricePerUnit());
        dto.setTotalAmount(s.getTotalAmount());
        dto.setSaleDate(s.getSaleDate());
        return dto;
    }
}
