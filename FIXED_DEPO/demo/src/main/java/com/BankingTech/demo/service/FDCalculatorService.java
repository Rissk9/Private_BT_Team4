package com.BankingTech.demo.service;

import com.BankingTech.demo.dto.requestdto.FDCalculationRequestDTO;
import com.BankingTech.demo.dto.responsedto.FDCalculationResponseDTO;

public interface FDCalculatorService {

    FDCalculationResponseDTO calculate(
            FDCalculationRequestDTO request
    );
}