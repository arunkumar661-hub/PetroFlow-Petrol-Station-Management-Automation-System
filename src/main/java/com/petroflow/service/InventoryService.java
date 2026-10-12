package com.petroflow.service;

import com.petroflow.dto.InventoryDto;
import com.petroflow.dto.StockAdjustmentDto;
import com.petroflow.entity.Fuel;
import com.petroflow.entity.Inventory;
import com.petroflow.exception.InvalidOperationException;
import com.petroflow.exception.ResourceNotFoundException;
import com.petroflow.repository.FuelRepository;
import com.petroflow.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final FuelRepository fuelRepository;

    public InventoryService(InventoryRepository inventoryRepository, FuelRepository fuelRepository) {
        this.inventoryRepository = inventoryRepository;
        this.fuelRepository = fuelRepository;
    }

    @Transactional(readOnly = true)
    public List<InventoryDto> getAllInventory() {
        return inventoryRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InventoryDto getInventoryByFuelCode(String fuelCode) {
        Inventory inv = inventoryRepository.findByFuel_FuelCode(fuelCode.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for fuel: " + fuelCode));
        return mapToDto(inv);
    }

    @Transactional
    public InventoryDto adjustStock(StockAdjustmentDto dto) {
        Fuel fuel = fuelRepository.findByFuelCode(dto.getFuelCode().toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Fuel not found with code: " + dto.getFuelCode()));

        Inventory inventory = inventoryRepository.findByFuelWithLock(fuel)
                .orElseGet(() -> new Inventory(fuel, BigDecimal.ZERO, new BigDecimal("100.00")));

        if ("ADD".equalsIgnoreCase(dto.getAction())) {
            inventory.setCurrentQuantity(inventory.getCurrentQuantity().add(dto.getQuantity()));
        } else if ("SET".equalsIgnoreCase(dto.getAction())) {
            if (dto.getQuantity().compareTo(BigDecimal.ZERO) < 0) {
                throw new InvalidOperationException("Stock quantity cannot be negative");
            }
            inventory.setCurrentQuantity(dto.getQuantity());
        } else {
            throw new InvalidOperationException("Invalid action. Must be ADD or SET");
        }

        Inventory saved = inventoryRepository.save(inventory);
        return mapToDto(saved);
    }

    @Transactional
    public InventoryDto updateMinimumStockLevel(String fuelCode, BigDecimal minLevel) {
        Inventory inv = inventoryRepository.findByFuel_FuelCode(fuelCode.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for fuel: " + fuelCode));

        if (minLevel.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidOperationException("Minimum stock level cannot be negative");
        }
        inv.setMinimumStockLevel(minLevel);
        return mapToDto(inventoryRepository.save(inv));
    }

    public InventoryDto mapToDto(Inventory inv) {
        return new InventoryDto(
                inv.getFuel().getFuelCode(),
                inv.getFuel().getFuelName(),
                inv.getCurrentQuantity(),
                inv.getFuel().getUnit(),
                inv.getMinimumStockLevel(),
                inv.isLowStock(),
                inv.getLastUpdated()
        );
    }
}
