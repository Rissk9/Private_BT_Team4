package com.BankingTech.demo.controller;

import com.BankingTech.demo.dto.requestdto.FixedDepositAccountRequestDTO;
import com.BankingTech.demo.dto.responsedto.FixedDepositAccountResponseDTO;
import com.BankingTech.demo.dto.responsedto.FDAccountCreationResponseDTO;
import com.BankingTech.demo.service.FixedDepositAccountService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fd/account")
public class FixedDepositAccountController {

        private final FixedDepositAccountService fixedDepositAccountService;

        public FixedDepositAccountController(
                        FixedDepositAccountService fixedDepositAccountService) {
                this.fixedDepositAccountService = fixedDepositAccountService;
        }

        @PostMapping("/create")
        public ResponseEntity<FixedDepositAccountResponseDTO> createAccount(
                        @RequestBody FixedDepositAccountRequestDTO request) {

                return ResponseEntity.ok(
                                fixedDepositAccountService.createAccount(request));
        }

        // L16
        @PostMapping("/create-with-txn")
        public ResponseEntity<FDAccountCreationResponseDTO> createAccountWithTransaction(
                        @RequestBody FixedDepositAccountRequestDTO request) {

                return ResponseEntity.ok(
                                fixedDepositAccountService.createAccountWithTransaction(request));
        }

        @GetMapping("/{fdAccountNo}")
        public ResponseEntity<FixedDepositAccountResponseDTO> getAccount(
                        @PathVariable String fdAccountNo) {

                return ResponseEntity.ok(
                                fixedDepositAccountService.getAccount(fdAccountNo));
        }

        @GetMapping("/customer/{customerId}")
        public ResponseEntity<List<FixedDepositAccountResponseDTO>> getAccountsByCustomer(
                        @PathVariable String customerId) {

                return ResponseEntity.ok(
                                fixedDepositAccountService.getAccountsByCustomer(customerId));
        }

        @GetMapping("/status/{status}")
        public ResponseEntity<List<FixedDepositAccountResponseDTO>> getAccountsByStatus(
                        @PathVariable String status) {

                return ResponseEntity.ok(
                                fixedDepositAccountService.getAccountsByStatus(status));
        }

        @GetMapping("/customer/{customerId}/status/{status}")
        public ResponseEntity<List<FixedDepositAccountResponseDTO>> getCustomerAccountsByStatus(
                        @PathVariable String customerId,
                        @PathVariable String status) {

                return ResponseEntity.ok(
                                fixedDepositAccountService.getCustomerAccountsByStatus(
                                                customerId,
                                                status));
        }
}