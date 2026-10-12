package com.petroflow.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class DashboardSummaryDto {
    private long todaySalesCount;
    private BigDecimal todayRevenue;
    private BigDecimal todayFuelDispensedLiters;
    private String activePumpsRatio;
    private long totalPumps;
    private long onlinePumps;
    private List<InventoryDto> inventoryStatus;
    private List<SaleDto> recentSales;
    private Map<String, BigDecimal> fuelWiseSales;
    private Map<String, BigDecimal> last7DaysRevenue;

    public DashboardSummaryDto() {}

    public long getTodaySalesCount() { return todaySalesCount; }
    public void setTodaySalesCount(long todaySalesCount) { this.todaySalesCount = todaySalesCount; }
    public BigDecimal getTodayRevenue() { return todayRevenue; }
    public void setTodayRevenue(BigDecimal todayRevenue) { this.todayRevenue = todayRevenue; }
    public BigDecimal getTodayFuelDispensedLiters() { return todayFuelDispensedLiters; }
    public void setTodayFuelDispensedLiters(BigDecimal todayFuelDispensedLiters) { this.todayFuelDispensedLiters = todayFuelDispensedLiters; }
    public String getActivePumpsRatio() { return activePumpsRatio; }
    public void setActivePumpsRatio(String activePumpsRatio) { this.activePumpsRatio = activePumpsRatio; }
    public long getTotalPumps() { return totalPumps; }
    public void setTotalPumps(long totalPumps) { this.totalPumps = totalPumps; }
    public long getOnlinePumps() { return onlinePumps; }
    public void setOnlinePumps(long onlinePumps) { this.onlinePumps = onlinePumps; }
    public List<InventoryDto> getInventoryStatus() { return inventoryStatus; }
    public void setInventoryStatus(List<InventoryDto> inventoryStatus) { this.inventoryStatus = inventoryStatus; }
    public List<SaleDto> getRecentSales() { return recentSales; }
    public void setRecentSales(List<SaleDto> recentSales) { this.recentSales = recentSales; }
    public Map<String, BigDecimal> getFuelWiseSales() { return fuelWiseSales; }
    public void setFuelWiseSales(Map<String, BigDecimal> fuelWiseSales) { this.fuelWiseSales = fuelWiseSales; }
    public Map<String, BigDecimal> getLast7DaysRevenue() { return last7DaysRevenue; }
    public void setLast7DaysRevenue(Map<String, BigDecimal> last7DaysRevenue) { this.last7DaysRevenue = last7DaysRevenue; }
}
