package com.woopaca.jissisplit.collector.websocket;

import com.woopaca.jissisplit.collector.websocket.dto.StockPriceResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class RealTimeStockPriceMessageParser {

    private static final int DATA_LENGTH = 26;
    private static final int STOCK_CODE_INDEX = 1;
    private static final int KOREAN_DATE_INDEX = 6;
    private static final int KOREAN_TIME_INDEX = 7;
    private static final int STOCK_PRICE_INDEX = 11;
    private static final int FLUCTUATION_RATE_INDEX = 14;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.BASIC_ISO_DATE;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HHmmss");

    public StockPriceResponse parse(String message) {
        String[] sections = message.split("\\|");
        String payload = sections[sections.length - 1];

        String[] data = payload.split("\\^");

        int totalSets = data.length / DATA_LENGTH;
        int startIndex = (totalSets - 1) * DATA_LENGTH;

        String stockCode = data[startIndex + STOCK_CODE_INDEX];
        LocalDate koreanDate = LocalDate.parse(data[startIndex + KOREAN_DATE_INDEX], DATE_FORMATTER);
        LocalTime koreanTime = LocalTime.parse(data[startIndex + KOREAN_TIME_INDEX], TIME_FORMATTER);
        BigDecimal stockPrice = new BigDecimal(data[startIndex + STOCK_PRICE_INDEX]);
        double stockFluctuationRate = Double.parseDouble(data[startIndex + FLUCTUATION_RATE_INDEX]);

        return new StockPriceResponse(stockCode, koreanDate, koreanTime, stockPrice, stockFluctuationRate);
    }
}
