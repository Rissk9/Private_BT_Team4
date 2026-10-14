package com.BankingTech.demo.controller;

import com.BankingTech.demo.service.MaturityProcessingService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/fd/maturity")
public class FDMaturityBatchController {

    private final MaturityProcessingService maturityProcessingService;

    public FDMaturityBatchController(
            MaturityProcessingService maturityProcessingService) {

        this.maturityProcessingService = maturityProcessingService;
    }

    @PostMapping("/process")
    public ResponseEntity<?> processMaturityBatch(
            @RequestParam(required = false) LocalDate businessDate) {

        if (businessDate == null) {
            businessDate = LocalDate.now();
        }

        maturityProcessingService.processMaturedAccounts(
                businessDate);

        return ResponseEntity.ok(
                Map.of(
                        "status", "COMPLETED",
                        "businessDate", businessDate,
                        "message",
                        "FD maturity batch processing completed"));
    }
}