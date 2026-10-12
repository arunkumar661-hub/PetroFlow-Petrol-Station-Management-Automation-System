package com.petroflow.service;

import com.petroflow.dto.ReportFilterDto;
import com.petroflow.dto.SaleDto;
import com.petroflow.entity.Sale;
import com.petroflow.repository.SaleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final SaleRepository saleRepository;
    private final SaleService saleService;

    public ReportService(SaleRepository saleRepository, SaleService saleService) {
        this.saleRepository = saleRepository;
        this.saleService = saleService;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> generateSalesReport(ReportFilterDto filter) {
        List<Sale> sales = saleRepository.findAllByOrderBySaleDateDesc();

        // Filter by Date
        if (filter.getStartDate() != null) {
            LocalDateTime start = filter.getStartDate().atStartOfDay();
            sales = sales.stream().filter(s -> !s.getSaleDate().isBefore(start)).collect(Collectors.toList());
        }
        if (filter.getEndDate() != null) {
            LocalDateTime end = filter.getEndDate().atTime(LocalTime.MAX);
            sales = sales.stream().filter(s -> !s.getSaleDate().isAfter(end)).collect(Collectors.toList());
        }

        // Filter by Fuel
        if (filter.getFuelCode() != null && !filter.getFuelCode().isBlank()) {
            sales = sales.stream().filter(s -> s.getFuel().getFuelCode().equalsIgnoreCase(filter.getFuelCode())).collect(Collectors.toList());
        }

        // Filter by Pump
        if (filter.getPumpCode() != null && !filter.getPumpCode().isBlank()) {
            sales = sales.stream().filter(s -> s.getPump().getPumpCode().equalsIgnoreCase(filter.getPumpCode())).collect(Collectors.toList());
        }

        // Filter by Employee
        if (filter.getEmployeeCode() != null && !filter.getEmployeeCode().isBlank()) {
            sales = sales.stream().filter(s -> s.getEmployee().getEmployeeCode().equalsIgnoreCase(filter.getEmployeeCode())).collect(Collectors.toList());
        }

        BigDecimal totalRevenue = BigDecimal.ZERO;
        BigDecimal totalQuantity = BigDecimal.ZERO;
        Map<String, BigDecimal> fuelRevenue = new LinkedHashMap<>();
        Map<String, BigDecimal> employeeRevenue = new LinkedHashMap<>();
        Map<String, BigDecimal> pumpRevenue = new LinkedHashMap<>();

        for (Sale s : sales) {
            totalRevenue = totalRevenue.add(s.getTotalAmount());
            totalQuantity = totalQuantity.add(s.getQuantity());

            String fuelName = s.getFuel().getFuelName();
            fuelRevenue.put(fuelName, fuelRevenue.getOrDefault(fuelName, BigDecimal.ZERO).add(s.getTotalAmount()));

            String empName = s.getEmployee().getName() + " (" + s.getEmployee().getEmployeeCode() + ")";
            employeeRevenue.put(empName, employeeRevenue.getOrDefault(empName, BigDecimal.ZERO).add(s.getTotalAmount()));

            String pumpCode = s.getPump().getPumpCode();
            pumpRevenue.put(pumpCode, pumpRevenue.getOrDefault(pumpCode, BigDecimal.ZERO).add(s.getTotalAmount()));
        }

        List<SaleDto> saleDtos = sales.stream().map(saleService::mapToDto).collect(Collectors.toList());

        Map<String, Object> report = new HashMap<>();
        report.put("totalTransactions", sales.size());
        report.put("totalRevenue", totalRevenue);
        report.put("totalQuantityDispensed", totalQuantity);
        report.put("fuelWiseBreakdown", fuelRevenue);
        report.put("employeeWiseBreakdown", employeeRevenue);
        report.put("pumpWiseBreakdown", pumpRevenue);
        report.put("sales", saleDtos);

        return report;
    }
}
