package com.petroflow.service;

import com.petroflow.dto.FuelDto;
import com.petroflow.dto.InventoryDto;
import com.petroflow.dto.StockAdjustmentDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class InventoryServiceTest {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private FuelService fuelService;

    @Test
    void testStockAdjustmentAddAndSet() {
        FuelDto fuel = fuelService.createFuel(new FuelDto("BIO_DIESEL", "Bio Diesel B20", new BigDecimal("92.00"), "L", true));

        // Add 500 L
        StockAdjustmentDto addDto = new StockAdjustmentDto();
        addDto.setFuelCode("BIO_DIESEL");
        addDto.setQuantity(new BigDecimal("500.00"));
        addDto.setAction("ADD");

        InventoryDto afterAdd = inventoryService.adjustStock(addDto);
        assertEquals(new BigDecimal("500.00"), afterAdd.getCurrentQuantity());

        // Set to 850 L
        StockAdjustmentDto setDto = new StockAdjustmentDto();
        setDto.setFuelCode("BIO_DIESEL");
        setDto.setQuantity(new BigDecimal("850.00"));
        setDto.setAction("SET");

        InventoryDto afterSet = inventoryService.adjustStock(setDto);
        assertEquals(new BigDecimal("850.00"), afterSet.getCurrentQuantity());
    }

    @Test
    void testLowStockDetection() {
        FuelDto fuel = fuelService.createFuel(new FuelDto("LPG_AUTO", "Auto LPG", new BigDecimal("65.00"), "KG", true));
        inventoryService.updateMinimumStockLevel("LPG_AUTO", new BigDecimal("100.00"));

        StockAdjustmentDto setDto = new StockAdjustmentDto();
        setDto.setFuelCode("LPG_AUTO");
        setDto.setQuantity(new BigDecimal("40.00")); // Below minimum of 100
        setDto.setAction("SET");

        InventoryDto inv = inventoryService.adjustStock(setDto);
        assertTrue(inv.isLowStock(), "Stock should be flagged as LOW STOCK when below minimum");
    }
}
