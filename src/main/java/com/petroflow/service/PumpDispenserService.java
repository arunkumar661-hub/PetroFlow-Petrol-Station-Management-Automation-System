package com.petroflow.service;

import com.petroflow.dto.DispenseRequestDto;
import com.petroflow.dto.InvoiceDto;
import com.petroflow.entity.*;
import com.petroflow.exception.InsufficientStockException;
import com.petroflow.exception.InvalidOperationException;
import com.petroflow.exception.ResourceNotFoundException;
import com.petroflow.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
public class PumpDispenserService {

    private final PumpRepository pumpRepository;
    private final FuelRepository fuelRepository;
    private final InventoryRepository inventoryRepository;
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;
    private final PumpTransactionRepository pumpTransactionRepository;
    private final SaleRepository saleRepository;
    private final InvoiceRepository invoiceRepository;
    private final IdentifierGeneratorService identifierGeneratorService;

    public PumpDispenserService(PumpRepository pumpRepository,
                                FuelRepository fuelRepository,
                                InventoryRepository inventoryRepository,
                                EmployeeRepository employeeRepository,
                                CustomerRepository customerRepository,
                                PumpTransactionRepository pumpTransactionRepository,
                                SaleRepository saleRepository,
                                InvoiceRepository invoiceRepository,
                                IdentifierGeneratorService identifierGeneratorService) {
        this.pumpRepository = pumpRepository;
        this.fuelRepository = fuelRepository;
        this.inventoryRepository = inventoryRepository;
        this.employeeRepository = employeeRepository;
        this.customerRepository = customerRepository;
        this.pumpTransactionRepository = pumpTransactionRepository;
        this.saleRepository = saleRepository;
        this.invoiceRepository = invoiceRepository;
        this.identifierGeneratorService = identifierGeneratorService;
    }

    /**
     * Executes the complete, atomic pump dispensing workflow:
     * 1. Validate pump, fuel, employee, quantity
     * 2. Lock and verify inventory
     * 3. Create & persist PumpTransaction
     * 4. Deduct inventory
     * 5. Create & persist Sale
     * 6. Generate & persist Invoice
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public InvoiceDto processDispenseTransaction(DispenseRequestDto request) {
        // 1. Validation
        if (request.getQuantity() == null || request.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidOperationException("Dispense quantity must be greater than zero");
        }

        Pump pump = pumpRepository.findByPumpCode(request.getPumpCode().toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Pump not found: " + request.getPumpCode()));

        if ("OFFLINE".equalsIgnoreCase(pump.getStatus()) || "MAINTENANCE".equalsIgnoreCase(pump.getStatus())) {
            throw new InvalidOperationException("Pump " + pump.getPumpCode() + " is currently " + pump.getStatus());
        }

        Fuel fuel = fuelRepository.findByFuelCode(request.getFuelCode().toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Fuel not found: " + request.getFuelCode()));

        if (!fuel.isActive()) {
            throw new InvalidOperationException("Fuel " + fuel.getFuelCode() + " is currently inactive");
        }

        Employee employee = employeeRepository.findByEmployeeCode(request.getEmployeeCode().toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + request.getEmployeeCode()));

        // 2. Lock & Check Inventory with Pessimistic Lock
        Inventory inventory = inventoryRepository.findByFuelWithLock(fuel)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not initialized for fuel: " + fuel.getFuelCode()));

        if (inventory.getCurrentQuantity().compareTo(request.getQuantity()) < 0) {
            throw new InsufficientStockException(String.format("Insufficient stock for %s. Available: %s %s, Requested: %s %s",
                    fuel.getFuelName(), inventory.getCurrentQuantity().toPlainString(), fuel.getUnit(),
                    request.getQuantity().toPlainString(), fuel.getUnit()));
        }

        // Calculate amount
        BigDecimal quantity = request.getQuantity();
        BigDecimal pricePerUnit = fuel.getPricePerUnit();
        BigDecimal totalAmount = quantity.multiply(pricePerUnit).setScale(2, RoundingMode.HALF_UP);
        LocalDateTime now = LocalDateTime.now();

        // Generate Transaction Number
        String transactionNumber = identifierGeneratorService.generateTransactionNumber();

        // 3. Create PumpTransaction
        PumpTransaction pumpTxn = new PumpTransaction(
                transactionNumber,
                pump,
                fuel,
                employee,
                quantity,
                fuel.getUnit(),
                pricePerUnit,
                totalAmount,
                "COMPLETED",
                now
        );
        PumpTransaction savedTxn = pumpTransactionRepository.save(pumpTxn);

        // 4. Deduct Inventory
        inventory.setCurrentQuantity(inventory.getCurrentQuantity().subtract(quantity));
        inventoryRepository.save(inventory);

        // 5. Create Sale
        Sale sale = new Sale(
                transactionNumber,
                savedTxn,
                fuel,
                pump,
                employee,
                quantity,
                fuel.getUnit(),
                pricePerUnit,
                totalAmount,
                now
        );
        Sale savedSale = saleRepository.save(sale);

        // Handle Customer info if provided
        String customerName = "Walk-in Customer";
        String vehicleNumber = request.getVehicleNumber();
        if (request.getCustomerName() != null && !request.getCustomerName().isBlank()) {
            customerName = request.getCustomerName().trim();
        }

        // 6. Generate Invoice
        String invoiceNumber = identifierGeneratorService.generateInvoiceNumber();
        Invoice invoice = new Invoice(
                invoiceNumber,
                savedSale,
                transactionNumber,
                customerName,
                vehicleNumber,
                pump.getPumpCode(),
                fuel.getFuelName(),
                quantity,
                fuel.getUnit(),
                pricePerUnit,
                totalAmount,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                totalAmount,
                request.getPaymentMethod() != null ? request.getPaymentMethod().toUpperCase() : "CASH",
                now
        );
        Invoice savedInvoice = invoiceRepository.save(invoice);

        return mapToInvoiceDto(savedInvoice);
    }

    private InvoiceDto mapToInvoiceDto(Invoice inv) {
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
