package com.petroflow.service;

import com.petroflow.dto.PumpDto;
import com.petroflow.entity.Fuel;
import com.petroflow.entity.Pump;
import com.petroflow.entity.PumpTransaction;
import com.petroflow.exception.InvalidOperationException;
import com.petroflow.exception.ResourceNotFoundException;
import com.petroflow.repository.FuelRepository;
import com.petroflow.repository.PumpRepository;
import com.petroflow.repository.PumpTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PumpService {

    private final PumpRepository pumpRepository;
    private final FuelRepository fuelRepository;
    private final PumpTransactionRepository pumpTransactionRepository;
    private final IdentifierGeneratorService identifierGeneratorService;

    public PumpService(PumpRepository pumpRepository,
                       FuelRepository fuelRepository,
                       PumpTransactionRepository pumpTransactionRepository,
                       IdentifierGeneratorService identifierGeneratorService) {
        this.pumpRepository = pumpRepository;
        this.fuelRepository = fuelRepository;
        this.pumpTransactionRepository = pumpTransactionRepository;
        this.identifierGeneratorService = identifierGeneratorService;
    }

    @Transactional(readOnly = true)
    public List<PumpDto> getAllPumps() {
        return pumpRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PumpDto getPumpByCode(String pumpCode) {
        Pump pump = pumpRepository.findByPumpCode(pumpCode.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Pump not found with code: " + pumpCode));
        return mapToDto(pump);
    }

    @Transactional
    public PumpDto createPump(PumpDto dto) {
        String pumpCode = identifierGeneratorService.generatePumpCode();
        Fuel fuel = fuelRepository.findByFuelCode(dto.getSupportedFuelCode().toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Supported fuel not found: " + dto.getSupportedFuelCode()));

        Pump pump = new Pump(
                pumpCode,
                dto.getPumpName().trim(),
                dto.getStatus() != null ? dto.getStatus().toUpperCase() : "ONLINE",
                fuel
        );
        Pump saved = pumpRepository.save(pump);
        return mapToDto(saved);
    }

    @Transactional
    public PumpDto updatePumpStatus(String pumpCode, String status) {
        Pump pump = pumpRepository.findByPumpCode(pumpCode.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Pump not found with code: " + pumpCode));

        String st = status.toUpperCase();
        if (!st.equals("ONLINE") && !st.equals("OFFLINE") && !st.equals("MAINTENANCE") && !st.equals("DISPENSING")) {
            throw new InvalidOperationException("Invalid pump status: " + status);
        }
        pump.setStatus(st);
        Pump saved = pumpRepository.save(pump);
        return mapToDto(saved);
    }

    public PumpDto mapToDto(Pump pump) {
        PumpDto dto = new PumpDto(
                pump.getPumpCode(),
                pump.getPumpName(),
                pump.getStatus(),
                pump.getSupportedFuel().getFuelCode(),
                pump.getSupportedFuel().getFuelName(),
                pump.getSupportedFuel().getPricePerUnit(),
                pump.getSupportedFuel().getUnit()
        );
        Optional<PumpTransaction> lastTxn = pumpTransactionRepository.findTopByPump_PumpCodeOrderByTransactionDateDesc(pump.getPumpCode());
        if (lastTxn.isPresent()) {
            dto.setLastTransactionNumber(lastTxn.get().getTransactionNumber());
            dto.setLastDispensedQuantity(lastTxn.get().getQuantity());
        }
        return dto;
    }
}
