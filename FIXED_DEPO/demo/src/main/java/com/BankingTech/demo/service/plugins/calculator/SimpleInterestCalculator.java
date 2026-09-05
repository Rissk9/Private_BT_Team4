package com.BankingTech.demo.service.plugins.calculator;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class SimpleInterestCalculator
        implements InterestCalculator {

    @Override
    public BigDecimal calculate(
            BigDecimal principal,
            BigDecimal annualRate,
            Integer termMonths
    ) {

        BigDecimal termYears =
                BigDecimal.valueOf(termMonths)
                        .divide(
                                BigDecimal.valueOf(12),
                                10,
                                RoundingMode.HALF_UP
                        );

        return principal
                .multiply(annualRate)
                .multiply(termYears)
                .divide(
                        BigDecimal.valueOf(100),
                        2,
                        RoundingMode.HALF_UP
                );
    }
}