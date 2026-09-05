package com.BankingTech.demo.controller;

import com.BankingTech.demo.dto.requestdto.FDCalculationRequestDTO;
import com.BankingTech.demo.dto.responsedto.FDCalculationResponseDTO;
import com.BankingTech.demo.service.FDCalculatorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fd")
public class FDCalculatorController {

        private final FDCalculatorService fdCalculatorService;

        public FDCalculatorController(
                        FDCalculatorService fdCalculatorService) {
                this.fdCalculatorService = fdCalculatorService;
        }

        @PostMapping("/calculate")
        public ResponseEntity<FDCalculationResponseDTO> calculate(
                        @Valid @RequestBody FDCalculationRequestDTO request) {

                /*
                 * No authentication annotation for now.
                 *
                 * Later, if calculator access requires
                 * authentication, the security annotation
                 * can be added after Auth integration.
                 */

                return ResponseEntity.ok(
                                fdCalculatorService.calculate(
                                                request));
        }
}