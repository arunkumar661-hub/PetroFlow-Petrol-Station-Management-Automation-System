package com.petroflow.config;

import com.petroflow.entity.*;
import com.petroflow.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final FuelRepository fuelRepository;
    private final InventoryRepository inventoryRepository;
    private final EmployeeRepository employeeRepository;
    private final SupplierRepository supplierRepository;
    private final PumpRepository pumpRepository;

    public DataInitializer(FuelRepository fuelRepository,
                           InventoryRepository inventoryRepository,
                           EmployeeRepository employeeRepository,
                           SupplierRepository supplierRepository,
                           PumpRepository pumpRepository) {
        this.fuelRepository = fuelRepository;
        this.inventoryRepository = inventoryRepository;
        this.employeeRepository = employeeRepository;
        this.supplierRepository = supplierRepository;
        this.pumpRepository = pumpRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (fuelRepository.count() == 0) {
            System.out.println(">>> Initializing PetroFlow Sample Master Data...");

            // 1. Fuels
            Fuel petrol = fuelRepository.save(new Fuel("PETROL", "Petrol (Speed 95)", new BigDecimal("108.50"), "L", true));
            Fuel diesel = fuelRepository.save(new Fuel("DIESEL", "High Speed Diesel", new BigDecimal("95.00"), "L", true));
            Fuel cng = fuelRepository.save(new Fuel("CNG", "Compressed Natural Gas", new BigDecimal("85.00"), "KG", true));

            // 2. Inventory
            inventoryRepository.save(new Inventory(petrol, new BigDecimal("1000.00"), new BigDecimal("150.00")));
            inventoryRepository.save(new Inventory(diesel, new BigDecimal("1000.00"), new BigDecimal("150.00")));
            inventoryRepository.save(new Inventory(cng, new BigDecimal("500.00"), new BigDecimal("80.00")));

            // 3. Employees
            employeeRepository.save(new Employee("EMP-001", "Arun Kumar", "+91 9876543210", "arun.kumar@petroflow.com", "Fuel Attendant", new BigDecimal("25000.00"), "ACTIVE", LocalDate.of(2024, 1, 15)));
            employeeRepository.save(new Employee("EMP-002", "Priya Sharma", "+91 9876543211", "priya.sharma@petroflow.com", "Station Supervisor", new BigDecimal("38000.00"), "ACTIVE", LocalDate.of(2023, 6, 10)));
            employeeRepository.save(new Employee("EMP-003", "Rajesh Patel", "+91 9876543212", "rajesh.patel@petroflow.com", "Fuel Attendant", new BigDecimal("25000.00"), "ACTIVE", LocalDate.of(2024, 3, 1)));

            // 4. Suppliers
            supplierRepository.save(new Supplier("SUP-001", "Bharat Petroleum Corp Ltd", "+91 22 22713000", "orders@bpcl-terminal.com", "Refinery Terminal Road, Sector 4", "ACTIVE"));
            supplierRepository.save(new Supplier("SUP-002", "Indian Oil Corporation", "+91 22 26447000", "supply@iocl-depot.com", "Main Terminal Road, Hub 2", "ACTIVE"));

            // 5. Pumps
            pumpRepository.save(new Pump("PUMP-01", "Dispenser 01 - Island North", "ONLINE", petrol));
            pumpRepository.save(new Pump("PUMP-02", "Dispenser 02 - Island North", "ONLINE", diesel));
            pumpRepository.save(new Pump("PUMP-03", "Dispenser 03 - Island South", "ONLINE", petrol));
            pumpRepository.save(new Pump("PUMP-04", "Dispenser 04 - Island South", "ONLINE", cng));

            System.out.println(">>> PetroFlow Sample Data Seeded Successfully!");
        }
    }
}
