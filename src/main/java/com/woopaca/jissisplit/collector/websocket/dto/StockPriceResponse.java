package com.woopaca.jissisplit.collector.websocket.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public record StockPriceResponse(String stockCode, LocalDate koreanDate, LocalTime koreanTime, BigDecimal stockPrice,
                                 double stockFluctuationRate) {
}
