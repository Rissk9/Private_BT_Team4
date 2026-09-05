package com.BankingTech.demo.service;

import com.BankingTech.demo.dto.requestdto.FDWithdrawalRequestDTO;
import com.BankingTech.demo.dto.responsedto.FDWithdrawalResponseDTO;

public interface FDWithdrawalService {

    FDWithdrawalResponseDTO processWithdrawal(
            FDWithdrawalRequestDTO request);
}