package com.petroflow.service;

import com.petroflow.dto.DashboardSummaryDto;
import com.petroflow.dto.InventoryDto;
import com.petroflow.dto.SaleDto;
import com.petroflow.entity.Sale;
import com.petroflow.repository.InventoryRepository;
import com.petroflow.repository.PumpRepository;
import com.petroflow.repository.SaleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final SaleRepository saleRepository;
    private final PumpRepository pumpRepository;
    private final InventoryRepository inventoryRepository;
    private final InventoryService inventoryService;
    private final SaleService saleService;

    public DashboardService(SaleRepository saleRepository,
                            PumpRepository pumpRepository,
                            InventoryRepository inventoryRepository,
                            InventoryService inventoryService,
                            SaleService saleService) {
        this.saleRepository = saleRepository;
        this.pumpRepository = pumpRepository;
        this.inventoryRepository = inventoryRepository;
        this.inventoryService = inventoryService;
        this.saleService = saleService;
    }

    @Transactional(readOnly = true)
    public DashboardSummaryDto getDashboardSummary() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        long todaySales = saleRepository.countTodaySales(startOfDay, endOfDay);
        BigDecimal todayRevenue = saleRepository.sumTodayRevenue(startOfDay, endOfDay);
        BigDecimal todayFuel = saleRepository.sumTodayFuelDispensed(startOfDay, endOfDay);

        long totalPumps = pumpRepository.count();
        long onlinePumps = pumpRepository.findByStatus("ONLINE").size() + pumpRepository.findByStatus("DISPENSING").size();

        List<InventoryDto> invList = inventoryRepository.findAll().stream()
                .map(inventoryService::mapToDto)
                .collect(Collectors.toList());

        List<SaleDto> recentSales = saleRepository.findAllByOrderBySaleDateDesc().stream()
                .limit(10)
                .map(saleService::mapToDto)
                .collect(Collectors.toList());

        // Fuel-wise breakdown for today / all time
        List<Sale> allSales = saleRepository.findAll();
        Map<String, BigDecimal> fuelWiseSales = new LinkedHashMap<>();
        for (Sale s : allSales) {
            String fuelName = s.getFuel().getFuelName();
            fuelWiseSales.put(fuelName, fuelWiseSales.getOrDefault(fuelName, BigDecimal.ZERO).add(s.getTotalAmount()));
        }

        // Last 7 days revenue trend
        Map<String, BigDecimal> last7Days = new LinkedHashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM");
        for (int i = 6; i >= 0; i--) {
            LocalDate day = today.minusDays(i);
            LocalDateTime dStart = day.atStartOfDay();
            LocalDateTime dEnd = day.atTime(LocalTime.MAX);
            BigDecimal dayRev = saleRepository.sumTodayRevenue(dStart, dEnd);
            last7Days.put(day.format(formatter), dayRev != null ? dayRev : BigDecimal.ZERO);
        }

        DashboardSummaryDto dto = new DashboardSummaryDto();
        dto.setTodaySalesCount(todaySales);
        dto.setTodayRevenue(todayRevenue != null ? todayRevenue : BigDecimal.ZERO);
        dto.setTodayFuelDispensedLiters(todayFuel != null ? todayFuel : BigDecimal.ZERO);
        dto.setTotalPumps(totalPumps);
        dto.setOnlinePumps(onlinePumps);
        dto.setActivePumpsRatio(onlinePumps + " / " + totalPumps);
        dto.setInventoryStatus(invList);
        dto.setRecentSales(recentSales);
        dto.setFuelWiseSales(fuelWiseSales);
        dto.setLast7DaysRevenue(last7Days);

        return dto;
    }
}
