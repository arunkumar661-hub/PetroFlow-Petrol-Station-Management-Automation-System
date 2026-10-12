package com.petroflow.controller;

import com.petroflow.dto.ApiResponse;
import com.petroflow.dto.PumpDto;
import com.petroflow.service.PumpService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pumps")
public class PumpController {

    private final PumpService pumpService;

    public PumpController(PumpService pumpService) {
        this.pumpService = pumpService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PumpDto>>> getAllPumps() {
        return ResponseEntity.ok(ApiResponse.success(pumpService.getAllPumps()));
    }

    @GetMapping("/{pumpCode}")
    public ResponseEntity<ApiResponse<PumpDto>> getPump(@PathVariable String pumpCode) {
        return ResponseEntity.ok(ApiResponse.success(pumpService.getPumpByCode(pumpCode)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PumpDto>> createPump(@Valid @RequestBody PumpDto dto) {
        PumpDto created = pumpService.createPump(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Pump created successfully", created));
    }

    @PutMapping("/{pumpCode}/status")
    public ResponseEntity<ApiResponse<PumpDto>> updatePumpStatus(
            @PathVariable String pumpCode,
            @RequestParam("status") String status) {
        PumpDto updated = pumpService.updatePumpStatus(pumpCode, status);
        return ResponseEntity.ok(ApiResponse.success("Pump status updated", updated));
    }
}
