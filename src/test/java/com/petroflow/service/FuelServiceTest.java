package com.petroflow.service;

import com.petroflow.dto.FuelDto;
import com.petroflow.entity.Fuel;
import com.petroflow.exception.InvalidOperationException;
import com.petroflow.repository.FuelRepository;
import com.petroflow.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class FuelServiceTest {

    @Autowired
    private FuelService fuelService;

    @Autowired
    private FuelRepository fuelRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Test
    void testCreateAndRetrieveFuel() {
        FuelDto newFuel = new FuelDto("PREMIUM_PETROL", "Speed 97 Octane", new BigDecimal("115.00"), "L", true);
        FuelDto created = fuelService.createFuel(newFuel);

        assertNotNull(created);
        assertEquals("PREMIUM_PETROL", created.getFuelCode());
        assertEquals("Speed 97 Octane", created.getFuelName());
        assertEquals(new BigDecimal("115.00"), created.getPricePerUnit());
        assertEquals("L", created.getUnit());

        FuelDto retrieved = fuelService.getFuelByCode("PREMIUM_PETROL");
        assertEquals("Speed 97 Octane", retrieved.getFuelName());

        // Check inventory record was auto-initialized
        assertTrue(inventoryRepository.findByFuel_FuelCode("PREMIUM_PETROL").isPresent());
    }

    @Test
    void testDuplicateFuelCodeThrowsException() {
        FuelDto fuel1 = new FuelDto("TEST_FUEL", "Test Fuel 1", new BigDecimal("100.00"), "L", true);
        fuelService.createFuel(fuel1);

        FuelDto fuel2 = new FuelDto("TEST_FUEL", "Test Fuel 2", new BigDecimal("105.00"), "L", true);
        assertThrows(InvalidOperationException.class, () -> fuelService.createFuel(fuel2));
    }

    @Test
    void testInvalidUnitThrowsException() {
        FuelDto invalidUnitFuel = new FuelDto("INVALID_UNIT", "Invalid Unit Fuel", new BigDecimal("100.00"), "GALLONS", true);
        assertThrows(InvalidOperationException.class, () -> fuelService.createFuel(invalidUnitFuel));
    }
}
