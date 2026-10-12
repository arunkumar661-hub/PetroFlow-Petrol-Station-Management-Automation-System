package com.petroflow.controller;

import com.petroflow.dto.ApiResponse;
import com.petroflow.dto.FuelDto;
import com.petroflow.service.FuelService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/fuels")
public class FuelController {

    private final FuelService fuelService;

    public FuelController(FuelService fuelService) {
        this.fuelService = fuelService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<FuelDto>>> getAllFuels(@RequestParam(value = "activeOnly", defaultValue = "false") boolean activeOnly) {
        List<FuelDto> fuels = activeOnly ? fuelService.getActiveFuels() : fuelService.getAllFuels();
        return ResponseEntity.ok(ApiResponse.success(fuels));
    }

    @GetMapping("/{fuelCode}")
    public ResponseEntity<ApiResponse<FuelDto>> getFuel(@PathVariable String fuelCode) {
        return ResponseEntity.ok(ApiResponse.success(fuelService.getFuelByCode(fuelCode)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FuelDto>> createFuel(@Valid @RequestBody FuelDto dto) {
        FuelDto created = fuelService.createFuel(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Fuel created successfully", created));
    }

    @PutMapping("/{fuelCode}")
    public ResponseEntity<ApiResponse<FuelDto>> updateFuel(@PathVariable String fuelCode, @Valid @RequestBody FuelDto dto) {
        FuelDto updated = fuelService.updateFuel(fuelCode, dto);
        return ResponseEntity.ok(ApiResponse.success("Fuel updated successfully", updated));
    }
}
