package com.petroflow.controller;

import com.petroflow.dto.ApiResponse;
import com.petroflow.dto.SaleDto;
import com.petroflow.service.SaleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SaleDto>>> getAllSales() {
        return ResponseEntity.ok(ApiResponse.success(saleService.getAllSales()));
    }

    @GetMapping("/{transactionNumber}")
    public ResponseEntity<ApiResponse<SaleDto>> getSale(@PathVariable String transactionNumber) {
        return ResponseEntity.ok(ApiResponse.success(saleService.getSaleByTransactionNumber(transactionNumber)));
    }
}
