package com.woopaca.jissisplit.collector.websocket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.woopaca.jissisplit.stock.ExchangeType;
import lombok.Builder;

public record KISStockSubscription(Header header, Body body) {

    private static final String SUBSCRIPTION = "1";
    private static final String UNSUBSCRIPTION = "2";

    public static KISStockSubscription subscribeNasdaq(String stockCode, String approvalKey) {
        Header header = Header.builder()
                .approvalKey(approvalKey)
                .transactionType(SUBSCRIPTION)
                .build();
        Input input = Input.builder()
                .transactionKey(ExchangeType.NASDAQ.getSubscribeName().concat(stockCode))
                .build();
        Body body = new Body(input);
        return new KISStockSubscription(header, body);
    }

    public static KISStockSubscription unsubscribeNasdaq(String stockCode, String approvalKey) {
        Header header = Header.builder()
                .approvalKey(approvalKey)
                .transactionType(UNSUBSCRIPTION)
                .build();
        Input input = Input.builder()
                .transactionKey(ExchangeType.NASDAQ.getSubscribeName().concat(stockCode))
                .build();
        Body body = new Body(input);
        return new KISStockSubscription(header, body);
    }

    @Builder
    record Header(@JsonProperty("approval_key") String approvalKey,
                  @JsonProperty("custtype") String customerType,
                  @JsonProperty("tr_type") String transactionType,
                  @JsonProperty("content-type") String contentType) {

        Header {
            customerType = "P";
            contentType = "utf-8";
        }
    }

    record Body(Input input) {
    }

    @Builder
    record Input(@JsonProperty("tr_id") String transactionId,
                 @JsonProperty("tr_key") String transactionKey) {

        Input {
            transactionId = "HDFSCNT0";
        }
    }
}
