package com.woopaca.jissisplit.stock;

import lombok.Getter;

@Getter
public enum ExchangeType {

    NASDAQ("DNAS");

    private final String subscribeName;

    ExchangeType(String subscribeName) {
        this.subscribeName = subscribeName;
    }
}
