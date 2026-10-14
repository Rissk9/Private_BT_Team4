package com.BankingTech.demo.service.plugins.calculator;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CompoundInterestCalculator
                implements InterestCalculator {

        @Override
        public BigDecimal calculate(
                        BigDecimal principal,
                        BigDecimal annualRate,
                        Integer termMonths) {

                BigDecimal monthlyRate = annualRate
                                .divide(
                                                BigDecimal.valueOf(12 * 100),
                                                12,
                                                RoundingMode.HALF_UP);

                double base = BigDecimal.ONE
                                .add(monthlyRate)
                                .doubleValue();

                double maturity = principal.doubleValue()
                                * Math.pow(
                                                base,
                                                termMonths);

                return BigDecimal.valueOf(maturity)
                                .subtract(principal)
                                .setScale(
                                                2,
                                                RoundingMode.HALF_UP);
        }
}