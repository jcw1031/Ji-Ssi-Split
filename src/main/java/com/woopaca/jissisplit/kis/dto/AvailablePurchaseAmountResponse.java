package com.woopaca.jissisplit.kis.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.math.BigDecimal;

public record AvailablePurchaseAmountResponse(Output output) {

    public int availableQuantity() {
        return output.availableQuantity;
    }

    public BigDecimal availableAmount() {
        return output.availableAmount;
    }

    record Output(@JsonAlias("ord_psbl_qty") int availableQuantity,
                  @JsonAlias("ovrs_ord_psbl_amt") BigDecimal availableAmount) {
    }
}
