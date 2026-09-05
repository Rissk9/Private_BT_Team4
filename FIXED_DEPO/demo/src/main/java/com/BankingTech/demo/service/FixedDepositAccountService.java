package com.BankingTech.demo.service;

import com.BankingTech.demo.dto.requestdto.FixedDepositAccountRequestDTO;
import com.BankingTech.demo.dto.responsedto.FDAccountCreationResponseDTO;
import com.BankingTech.demo.dto.responsedto.FixedDepositAccountResponseDTO;

import java.util.List;

public interface FixedDepositAccountService {

        FixedDepositAccountResponseDTO createAccount(
                        FixedDepositAccountRequestDTO request);

        FixedDepositAccountResponseDTO getAccount(
                        String fdAccountNo);

        List<FixedDepositAccountResponseDTO> getAccountsByCustomer(
                        String customerId);

        List<FixedDepositAccountResponseDTO> getAccountsByStatus(
                        String status);

        List<FixedDepositAccountResponseDTO> getCustomerAccountsByStatus(
                        String customerId,
                        String status);

        FDAccountCreationResponseDTO createAccountWithTransaction(
                        FixedDepositAccountRequestDTO request);

}