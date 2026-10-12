package com.petroflow.service;

import com.petroflow.dto.InvoiceDto;
import com.petroflow.entity.Invoice;
import com.petroflow.exception.ResourceNotFoundException;
import com.petroflow.repository.InvoiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Transactional(readOnly = true)
    public List<InvoiceDto> getAllInvoices() {
        return invoiceRepository.findAllByOrderByInvoiceDateDesc().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InvoiceDto getInvoiceByNumber(String invoiceNumber) {
        Invoice inv = invoiceRepository.findByInvoiceNumber(invoiceNumber.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with number: " + invoiceNumber));
        return mapToDto(inv);
    }

    @Transactional(readOnly = true)
    public InvoiceDto getInvoiceByTransactionNumber(String transactionNumber) {
        Invoice inv = invoiceRepository.findByTransactionNumber(transactionNumber.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found for transaction: " + transactionNumber));
        return mapToDto(inv);
    }

    public InvoiceDto mapToDto(Invoice inv) {
        InvoiceDto dto = new InvoiceDto();
        dto.setInvoiceNumber(inv.getInvoiceNumber());
        dto.setTransactionNumber(inv.getTransactionNumber());
        dto.setCustomerName(inv.getCustomerName());
        dto.setVehicleNumber(inv.getVehicleNumber());
        dto.setPumpCode(inv.getPumpCode());
        dto.setFuelName(inv.getFuelName());
        dto.setQuantity(inv.getQuantity());
        dto.setUnit(inv.getUnit());
        dto.setPricePerUnit(inv.getPricePerUnit());
        dto.setSubtotal(inv.getSubtotal());
        dto.setTaxRate(inv.getTaxRate());
        dto.setTaxAmount(inv.getTaxAmount());
        dto.setTotalAmount(inv.getTotalAmount());
        dto.setPaymentMethod(inv.getPaymentMethod());
        dto.setInvoiceDate(inv.getInvoiceDate());
        return dto;
    }
}
