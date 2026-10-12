package com.petroflow.controller;

import com.petroflow.dto.ApiResponse;
import com.petroflow.dto.ReportFilterDto;
import com.petroflow.service.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/sales")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSalesReport(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "fuelCode", required = false) String fuelCode,
            @RequestParam(value = "pumpCode", required = false) String pumpCode,
            @RequestParam(value = "employeeCode", required = false) String employeeCode) {

        ReportFilterDto filter = new ReportFilterDto();
        filter.setStartDate(startDate);
        filter.setEndDate(endDate);
        filter.setFuelCode(fuelCode);
        filter.setPumpCode(pumpCode);
        filter.setEmployeeCode(employeeCode);

        Map<String, Object> report = reportService.generateSalesReport(filter);
        return ResponseEntity.ok(ApiResponse.success(report));
    }
}
