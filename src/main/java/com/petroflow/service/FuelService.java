package com.petroflow.service;

import com.petroflow.dto.FuelDto;
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
public class FuelService {

    private final FuelRepository fuelRepository;
    private final InventoryRepository inventoryRepository;

    public FuelService(FuelRepository fuelRepository, InventoryRepository inventoryRepository) {
        this.fuelRepository = fuelRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional(readOnly = true)
    public List<FuelDto> getAllFuels() {
        return fuelRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FuelDto> getActiveFuels() {
        return fuelRepository.findByActiveTrue().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FuelDto getFuelByCode(String fuelCode) {
        Fuel fuel = fuelRepository.findByFuelCode(fuelCode.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Fuel not found with code: " + fuelCode));
        return mapToDto(fuel);
    }

    @Transactional
    public FuelDto createFuel(FuelDto dto) {
        String code = dto.getFuelCode() != null && !dto.getFuelCode().isBlank() 
                ? dto.getFuelCode().trim().toUpperCase() 
                : dto.getFuelName().replaceAll("\s+", "_").toUpperCase();

        if (fuelRepository.existsByFuelCode(code)) {
            throw new InvalidOperationException("Fuel already exists with code: " + code);
        }

        String unit = dto.getUnit() != null ? dto.getUnit().toUpperCase() : "L";
        if (!unit.equals("L") && !unit.equals("KG")) {
            throw new InvalidOperationException("Supported fuel units are 'L' and 'KG' only");
        }

        Fuel fuel = new Fuel(code, dto.getFuelName().trim(), dto.getPricePerUnit(), unit, dto.isActive());
        Fuel saved = fuelRepository.save(fuel);

        // Auto initialize inventory record if missing
        if (inventoryRepository.findByFuel(saved).isEmpty()) {
            Inventory inv = new Inventory(saved, BigDecimal.ZERO, new BigDecimal("100.00"));
            inventoryRepository.save(inv);
        }

        return mapToDto(saved);
    }

    @Transactional
    public FuelDto updateFuel(String fuelCode, FuelDto dto) {
        Fuel fuel = fuelRepository.findByFuelCode(fuelCode.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Fuel not found with code: " + fuelCode));

        fuel.setFuelName(dto.getFuelName().trim());
        fuel.setPricePerUnit(dto.getPricePerUnit());
        fuel.setActive(dto.isActive());
        if (dto.getUnit() != null && (dto.getUnit().equalsIgnoreCase("L") || dto.getUnit().equalsIgnoreCase("KG"))) {
            fuel.setUnit(dto.getUnit().toUpperCase());
        }

        Fuel updated = fuelRepository.save(fuel);
        return mapToDto(updated);
    }

    private FuelDto mapToDto(Fuel fuel) {
        return new FuelDto(fuel.getFuelCode(), fuel.getFuelName(), fuel.getPricePerUnit(), fuel.getUnit(), fuel.isActive());
    }
}
