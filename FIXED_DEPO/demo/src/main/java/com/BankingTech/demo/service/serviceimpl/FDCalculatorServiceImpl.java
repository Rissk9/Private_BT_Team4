package com.BankingTech.demo.service.serviceimpl;

import com.BankingTech.demo.dto.requestdto.FDCalculationRequestDTO;
import com.BankingTech.demo.dto.responsedto.FDCalculationResponseDTO;
import com.BankingTech.demo.service.FDCalculatorService;
import com.BankingTech.demo.service.plugins.calculator.CompoundInterestCalculator;
import com.BankingTech.demo.service.plugins.calculator.InterestCalculator;
import com.BankingTech.demo.service.plugins.calculator.SimpleInterestCalculator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class FDCalculatorServiceImpl
        implements FDCalculatorService {

    private final SimpleInterestCalculator
            simpleInterestCalculator;

    private final CompoundInterestCalculator
            compoundInterestCalculator;


    /*
     * Temporary development cap.
     *
     * Later this should come from Product & Pricing.
     */
    private static final BigDecimal CATEGORY_CAP =
            BigDecimal.valueOf(2.0);


    public FDCalculatorServiceImpl(
            SimpleInterestCalculator simpleInterestCalculator,
            CompoundInterestCalculator compoundInterestCalculator
    ) {

        this.simpleInterestCalculator =
                simpleInterestCalculator;

        this.compoundInterestCalculator =
                compoundInterestCalculator;
    }


    @Override
    public FDCalculationResponseDTO calculate(
            FDCalculationRequestDTO request
    ) {

        BigDecimal categoryAddition =
                calculateCategoryAddition(
                        request.getCategories()
                );

        if (categoryAddition.compareTo(
                CATEGORY_CAP
        ) > 0) {

            throw new IllegalArgumentException(
                    "Combined category rate exceeds configured cap"
            );
        }


        BigDecimal effectiveRate =
                request.getBaseRate()
                        .add(categoryAddition)
                        .setScale(
                                4,
                                RoundingMode.HALF_UP
                        );


        InterestCalculator calculator;


        if ("COMPOUND".equalsIgnoreCase(
                request.getInterestType()
        )) {

            calculator =
                    compoundInterestCalculator;

        } else if ("SIMPLE".equalsIgnoreCase(
                request.getInterestType()
        )) {

            calculator =
                    simpleInterestCalculator;

        } else {

            throw new IllegalArgumentException(
                    "Unsupported interest type: "
                            + request.getInterestType()
            );
        }


        BigDecimal interest =
                calculator.calculate(
                        request.getPrincipal(),
                        effectiveRate,
                        request.getTerm().getValue()
                );


        BigDecimal maturityAmount =
                request.getPrincipal()
                        .add(interest)
                        .setScale(
                                2,
                                RoundingMode.HALF_UP
                        );


        FDCalculationResponseDTO response =
                new FDCalculationResponseDTO();

        response.setMaturityAmount(
                maturityAmount
        );

        response.setEffectiveRate(
                effectiveRate
        );

        response.setInterestEarned(
                interest
        );

        return response;
    }


    private BigDecimal calculateCategoryAddition(
            java.util.List<String> categories
    ) {

        /*
         * Temporary implementation.
         *
         * Actual category additions will be
         * fetched from Product & Pricing.
         */

        BigDecimal addition =
                BigDecimal.ZERO;

        for (String category : categories) {

            if ("Senior Citizen".equalsIgnoreCase(category)) {

                addition =
                        addition.add(
                                BigDecimal.ONE
                        );

            } else if ("Employee".equalsIgnoreCase(category)) {

                addition =
                        addition.add(
                                BigDecimal.ONE
                        );
            }
        }

        return addition;
    }
}