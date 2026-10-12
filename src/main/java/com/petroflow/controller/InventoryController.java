package com.petroflow.controller;

import com.petroflow.dto.ApiResponse;
import com.petroflow.dto.InventoryDto;
import com.petroflow.dto.StockAdjustmentDto;
import com.petroflow.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<InventoryDto>>> getAllInventory() {
        return ResponseEntity.ok(ApiResponse.success(inventoryService.getAllInventory()));
    }

    @GetMapping("/{fuelCode}")
    public ResponseEntity<ApiResponse<InventoryDto>> getInventory(@PathVariable String fuelCode) {
        return ResponseEntity.ok(ApiResponse.success(inventoryService.getInventoryByFuelCode(fuelCode)));
    }

    @PostMapping("/adjust")
    public ResponseEntity<ApiResponse<InventoryDto>> adjustStock(@Valid @RequestBody StockAdjustmentDto dto) {
        InventoryDto updated = inventoryService.adjustStock(dto);
        return ResponseEntity.ok(ApiResponse.success("Inventory stock adjusted successfully", updated));
    }

    @PutMapping("/{fuelCode}/minimum-level")
    public ResponseEntity<ApiResponse<InventoryDto>> updateMinimumStockLevel(
            @PathVariable String fuelCode,
            @RequestParam("minimumLevel") BigDecimal minimumLevel) {
        InventoryDto updated = inventoryService.updateMinimumStockLevel(fuelCode, minimumLevel);
        return ResponseEntity.ok(ApiResponse.success("Minimum stock level updated", updated));
    }
}
