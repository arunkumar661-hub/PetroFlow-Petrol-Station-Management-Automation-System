package com.petroflow.service;

import com.petroflow.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Year;

@Service
public class IdentifierGeneratorService {

    private final EmployeeRepository employeeRepository;
    private final SupplierRepository supplierRepository;
    private final PumpRepository pumpRepository;
    private final CustomerRepository customerRepository;
    private final PumpTransactionRepository pumpTransactionRepository;
    private final InvoiceRepository invoiceRepository;

    public IdentifierGeneratorService(EmployeeRepository employeeRepository,
                                      SupplierRepository supplierRepository,
                                      PumpRepository pumpRepository,
                                      CustomerRepository customerRepository,
                                      PumpTransactionRepository pumpTransactionRepository,
                                      InvoiceRepository invoiceRepository) {
        this.employeeRepository = employeeRepository;
        this.supplierRepository = supplierRepository;
        this.pumpRepository = pumpRepository;
        this.customerRepository = customerRepository;
        this.pumpTransactionRepository = pumpTransactionRepository;
        this.invoiceRepository = invoiceRepository;
    }

    @Transactional(readOnly = true)
    public synchronized String generateEmployeeCode() {
        long count = employeeRepository.count() + 1;
        String code = String.format("EMP-%03d", count);
        while (employeeRepository.existsByEmployeeCode(code)) {
            count++;
            code = String.format("EMP-%03d", count);
        }
        return code;
    }

    @Transactional(readOnly = true)
    public synchronized String generateSupplierCode() {
        long count = supplierRepository.count() + 1;
        String code = String.format("SUP-%03d", count);
        while (supplierRepository.existsBySupplierCode(code)) {
            count++;
            code = String.format("SUP-%03d", count);
        }
        return code;
    }

    @Transactional(readOnly = true)
    public synchronized String generatePumpCode() {
        long count = pumpRepository.count() + 1;
        String code = String.format("PUMP-%02d", count);
        while (pumpRepository.existsByPumpCode(code)) {
            count++;
            code = String.format("PUMP-%02d", count);
        }
        return code;
    }

    @Transactional(readOnly = true)
    public synchronized String generateCustomerCode() {
        long count = customerRepository.count() + 1;
        return String.format("CUST-%04d", count);
    }

    @Transactional(readOnly = true)
    public synchronized String generateTransactionNumber() {
        int year = Year.now().getValue();
        long count = pumpTransactionRepository.count() + 1;
        String num = String.format("TXN-%d-%06d", year, count);
        while (pumpTransactionRepository.findByTransactionNumber(num).isPresent()) {
            count++;
            num = String.format("TXN-%d-%06d", year, count);
        }
        return num;
    }

    @Transactional(readOnly = true)
    public synchronized String generateInvoiceNumber() {
        int year = Year.now().getValue();
        long count = invoiceRepository.count() + 1;
        String num = String.format("INV-%d-%06d", year, count);
        while (invoiceRepository.findByInvoiceNumber(num).isPresent()) {
            count++;
            num = String.format("INV-%d-%06d", year, count);
        }
        return num;
    }
}
