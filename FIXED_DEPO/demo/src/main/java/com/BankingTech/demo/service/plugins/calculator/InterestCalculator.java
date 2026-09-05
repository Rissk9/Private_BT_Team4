package com.BankingTech.demo.service.plugins.calculator;

import java.math.BigDecimal;

public interface InterestCalculator {

    BigDecimal calculate(
            BigDecimal principal,
            BigDecimal annualRate,
            Integer termMonths
    );
}