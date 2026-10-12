package com.petroflow.controller;

import com.petroflow.dto.ApiResponse;
import com.petroflow.dto.DashboardSummaryDto;
import com.petroflow.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<DashboardSummaryDto>> getDashboard() {
        DashboardSummaryDto summary = dashboardService.getDashboardSummary();
        return ResponseEntity.ok(ApiResponse.success(summary));
    }
}
