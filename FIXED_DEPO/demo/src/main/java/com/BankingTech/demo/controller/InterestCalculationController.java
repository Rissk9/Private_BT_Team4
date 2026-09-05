package com.BankingTech.demo.controller;

import com.BankingTech.demo.service.InterestCalculationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fd/interest")
public class InterestCalculationController {

    private final InterestCalculationService interestCalculationService;

    public InterestCalculationController(
            InterestCalculationService interestCalculationService) {
        this.interestCalculationService = interestCalculationService;
    }

    @PostMapping("/calculate")
    public ResponseEntity<String> calculateInterest() {

        /*
         * Later:
         * 
         * @PreAuthorize("hasAnyRole('BANK_OFFICER', 'ADMIN')")
         */

        interestCalculationService
                .calculateInterestForActiveAccounts();

        return ResponseEntity.ok(
                "Interest calculation batch completed");
    }
}