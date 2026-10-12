package com.petroflow.controller;

import com.petroflow.dto.ApiResponse;
import com.petroflow.dto.InvoiceDto;
import com.petroflow.service.InvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<InvoiceDto>>> getAllInvoices() {
        return ResponseEntity.ok(ApiResponse.success(invoiceService.getAllInvoices()));
    }

    @GetMapping("/{invoiceNumber}")
    public ResponseEntity<ApiResponse<InvoiceDto>> getInvoice(@PathVariable String invoiceNumber) {
        return ResponseEntity.ok(ApiResponse.success(invoiceService.getInvoiceByNumber(invoiceNumber)));
    }

    @GetMapping("/transaction/{transactionNumber}")
    public ResponseEntity<ApiResponse<InvoiceDto>> getInvoiceByTxn(@PathVariable String transactionNumber) {
        return ResponseEntity.ok(ApiResponse.success(invoiceService.getInvoiceByTransactionNumber(transactionNumber)));
    }
}
