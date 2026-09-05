package com.BankingTech.demo.dto.requestdto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public class FDCalculationRequestDTO {

    @NotNull(message = "Principal is required")
    @DecimalMin(value = "0.01", message = "Principal must be greater than zero")
    private BigDecimal principal;

    @NotNull(message = "Term is required")
    @Valid
    private TermDTO term;

    @NotNull(message = "Base rate is required")
    @DecimalMin(value = "0.0", message = "Base rate cannot be negative")
    private BigDecimal baseRate;

    private List<String> categories;

    /*
     * Temporary until Product & Pricing integration.
     */
    @NotBlank(message = "Interest type is required")
    private String interestType;

    public BigDecimal getPrincipal() {
        return principal;
    }

    public void setPrincipal(BigDecimal principal) {
        this.principal = principal;
    }

    public TermDTO getTerm() {
        return term;
    }

    public void setTerm(TermDTO term) {
        this.term = term;
    }

    public BigDecimal getBaseRate() {
        return baseRate;
    }

    public void setBaseRate(BigDecimal baseRate) {
        this.baseRate = baseRate;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getInterestType() {
        return interestType;
    }

    public void setInterestType(String interestType) {
        this.interestType = interestType;
    }
}