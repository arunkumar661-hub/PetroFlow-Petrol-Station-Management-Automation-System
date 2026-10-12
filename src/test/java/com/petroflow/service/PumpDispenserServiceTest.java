package com.petroflow.service;

import com.petroflow.dto.DispenseRequestDto;
import com.petroflow.dto.EmployeeDto;
import com.petroflow.dto.FuelDto;
import com.petroflow.dto.InvoiceDto;
import com.petroflow.dto.PumpDto;
import com.petroflow.dto.StockAdjustmentDto;
import com.petroflow.exception.InsufficientStockException;
import com.petroflow.exception.InvalidOperationException;
import com.petroflow.repository.SaleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class PumpDispenserServiceTest {

    @Autowired
    private PumpDispenserService pumpDispenserService;

    @Autowired
    private FuelService fuelService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PumpService pumpService;

    @Autowired
    private SaleRepository saleRepository;

    @Test
    void testSuccessfulDispenseTransactionFlow() {
        // Setup master records
        FuelDto fuel = fuelService.createFuel(new FuelDto("SUPER_PETROL", "Super Petrol", new BigDecimal("108.50"), "L", true));
        
        StockAdjustmentDto stock = new StockAdjustmentDto();
        stock.setFuelCode("SUPER_PETROL");
        stock.setQuantity(new BigDecimal("1000.00"));
        stock.setAction("SET");
        inventoryService.adjustStock(stock);

        EmployeeDto emp = employeeService.createEmployee(new EmployeeDto(null, "Ramesh Nair", "+91 9988776655", "ramesh@petroflow.com", "Attendant", new BigDecimal("22000.00"), "ACTIVE", LocalDate.now()));

        PumpDto pump = pumpService.createPump(new PumpDto(null, "Pump Test Alpha", "ONLINE", "SUPER_PETROL", "Super Petrol", new BigDecimal("108.50"), "L"));

        // Execute Dispense
        DispenseRequestDto request = new DispenseRequestDto();
        request.setPumpCode(pump.getPumpCode());
        request.setFuelCode("SUPER_PETROL");
        request.setEmployeeCode(emp.getEmployeeCode());
        request.setQuantity(new BigDecimal("20.00"));
        request.setCustomerName("Raj Malhotra");
        request.setVehicleNumber("MH 02 AB 1234");
        request.setPaymentMethod("UPI");

        InvoiceDto invoice = pumpDispenserService.processDispenseTransaction(request);

        // Verify Invoice Output
        assertNotNull(invoice);
        assertNotNull(invoice.getInvoiceNumber());
        assertTrue(invoice.getInvoiceNumber().startsWith("INV-"));
        assertNotNull(invoice.getTransactionNumber());
        assertTrue(invoice.getTransactionNumber().startsWith("TXN-"));
        assertEquals("Raj Malhotra", invoice.getCustomerName());
        assertEquals("MH 02 AB 1234", invoice.getVehicleNumber());
        assertEquals(new BigDecimal("20.00"), invoice.getQuantity());
        assertEquals(new BigDecimal("108.50"), invoice.getPricePerUnit());
        assertEquals(new BigDecimal("2170.00"), invoice.getTotalAmount());

        // Verify Inventory Deduction (1000 - 20 = 980)
        var invAfter = inventoryService.getInventoryByFuelCode("SUPER_PETROL");
        assertEquals(new BigDecimal("980.00"), invAfter.getCurrentQuantity());

        // Verify Sale Record created in DB
        var sale = saleRepository.findByTransactionNumber(invoice.getTransactionNumber());
        assertTrue(sale.isPresent());
        assertEquals(new BigDecimal("2170.00"), sale.get().getTotalAmount());
    }

    @Test
    void testInsufficientStockThrowsExceptionAndRollsBack() {
        FuelDto fuel = fuelService.createFuel(new FuelDto("DIESEL_PLUS", "Diesel Plus", new BigDecimal("95.00"), "L", true));
        
        StockAdjustmentDto stock = new StockAdjustmentDto();
        stock.setFuelCode("DIESEL_PLUS");
        stock.setQuantity(new BigDecimal("15.00")); // Only 15 L in stock
        stock.setAction("SET");
        inventoryService.adjustStock(stock);

        EmployeeDto emp = employeeService.createEmployee(new EmployeeDto(null, "Suresh", "+91 9988776644", "suresh@petroflow.com", "Attendant", new BigDecimal("22000.00"), "ACTIVE", LocalDate.now()));
        PumpDto pump = pumpService.createPump(new PumpDto(null, "Pump Test Beta", "ONLINE", "DIESEL_PLUS", "Diesel Plus", new BigDecimal("95.00"), "L"));

        // Request 50 L (exceeds 15 L available)
        DispenseRequestDto request = new DispenseRequestDto();
        request.setPumpCode(pump.getPumpCode());
        request.setFuelCode("DIESEL_PLUS");
        request.setEmployeeCode(emp.getEmployeeCode());
        request.setQuantity(new BigDecimal("50.00"));

        assertThrows(InsufficientStockException.class, () -> pumpDispenserService.processDispenseTransaction(request));

        // Verify Stock Remains Untouched at 15 L
        var invAfter = inventoryService.getInventoryByFuelCode("DIESEL_PLUS");
        assertEquals(new BigDecimal("15.00"), invAfter.getCurrentQuantity());
    }

    @Test
    void testOfflinePumpRejectsDispensing() {
        FuelDto fuel = fuelService.createFuel(new FuelDto("TEST_CNG", "Test CNG", new BigDecimal("85.00"), "KG", true));
        EmployeeDto emp = employeeService.createEmployee(new EmployeeDto(null, "Karan", "+91 9988776633", "karan@petroflow.com", "Attendant", new BigDecimal("22000.00"), "ACTIVE", LocalDate.now()));
        PumpDto pump = pumpService.createPump(new PumpDto(null, "Pump Offline Gamma", "MAINTENANCE", "TEST_CNG", "Test CNG", new BigDecimal("85.00"), "KG"));

        DispenseRequestDto request = new DispenseRequestDto();
        request.setPumpCode(pump.getPumpCode());
        request.setFuelCode("TEST_CNG");
        request.setEmployeeCode(emp.getEmployeeCode());
        request.setQuantity(new BigDecimal("10.00"));

        assertThrows(InvalidOperationException.class, () -> pumpDispenserService.processDispenseTransaction(request));
    }
}
