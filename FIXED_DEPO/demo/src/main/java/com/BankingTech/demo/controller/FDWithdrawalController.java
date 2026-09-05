package com.BankingTech.demo.controller;

import com.BankingTech.demo.dto.requestdto.FDWithdrawalRequestDTO;
import com.BankingTech.demo.dto.responsedto.FDWithdrawalResponseDTO;
import com.BankingTech.demo.service.FDWithdrawalService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fd/account")
public class FDWithdrawalController {

    private final FDWithdrawalService fdWithdrawalService;

    public FDWithdrawalController(
            FDWithdrawalService fdWithdrawalService) {
        this.fdWithdrawalService = fdWithdrawalService;
    }

    @PostMapping("/withdraw")
    public ResponseEntity<FDWithdrawalResponseDTO> withdrawFD(
            @RequestBody FDWithdrawalRequestDTO request) {

        FDWithdrawalResponseDTO response = fdWithdrawalService.processWithdrawal(
                request);

        return ResponseEntity.ok(response);
    }
}