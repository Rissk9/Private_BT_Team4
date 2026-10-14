package com.BankingTech.demo.controller;

import com.BankingTech.demo.service.InterestCalculationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/fd/interest-batch")
public class FDInterestBatchController {

    private final InterestCalculationService interestCalculationService;

    public FDInterestBatchController(
            InterestCalculationService interestCalculationService) {
        this.interestCalculationService = interestCalculationService;
    }

    @PostMapping
    public String runInterestBatch(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate businessDate) {

        LocalDate effectiveDate = businessDate != null
                ? businessDate
                : LocalDate.now();

        interestCalculationService
                .calculateInterestForActiveAccounts(effectiveDate);

        return "FD interest batch processed for business date "
                + effectiveDate;
    }
}