package com.BankingTech.demo.dto.requestdto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class TermDTO {

    @NotNull(message = "Term value is required")
    @Positive(message = "Term value must be greater than zero")
    private Integer value;

    @NotNull(message = "Term unit is required")
    private String unit;


    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}