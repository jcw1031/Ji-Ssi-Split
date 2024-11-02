package com.woopaca.jissisplit.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woopaca.jissisplit.collector.websocket.dto.StockPriceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class StockTradeProcessor implements MessageListener {

    private final ObjectMapper objectMapper;

    private BigDecimal maxPrice;
    private BigDecimal buyPrice;
    private BigDecimal activationThreshold;
    private BigDecimal trailingStopRate;
    private BigDecimal dipRate;
    private BigDecimal buyAmount;

    public StockTradeProcessor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.maxPrice = BigDecimal.ZERO;
        this.buyPrice = BigDecimal.ZERO;
        this.activationThreshold = BigDecimal.valueOf(1.0);
        this.trailingStopRate = BigDecimal.valueOf(0.3);
        this.dipRate = BigDecimal.valueOf(1.0);
        this.buyAmount = BigDecimal.valueOf(1_000.00);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            StockPriceResponse body = objectMapper.readValue(message.getBody(), StockPriceResponse.class);
            BigDecimal stockPrice = body.stockPrice();
            if (buyPrice.equals(BigDecimal.ZERO)) {
                buyPrice = stockPrice;
            }

            if (isLessThan(buyPrice, stockPrice)) { // maxPrice 비교 및 갱신 및 트레일링 스탑 활성 여부 확인
                if (isLessThan(maxPrice, stockPrice)) { // maxPrice 비교 및 갱신
                    maxPrice = stockPrice;
                    return;
                }

                BigDecimal increaseRate = calculateIncreaseRate(buyPrice, maxPrice);
                if (isLessThan(increaseRate, activationThreshold)) { // 트레일링 스탑 활성화 여부 확인
                    return;
                }

                BigDecimal dropRate = calculateDropRate(maxPrice, stockPrice);
                if (isLessThan(trailingStopRate, dropRate)) { // 트레일링 스탑 조건 만족 확인
                    // TODO 매도
                }
                return;
            }

            BigDecimal dropRate = calculateDropRate(buyPrice, stockPrice);
            if (isLessThan(dipRate, dropRate)) {
                // TODO 추가 매수 및
                return;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isLessThan(BigDecimal basePrice, BigDecimal comparePrice) {
        return basePrice.compareTo(comparePrice) < 0;
    }

    private BigDecimal calculateDropRate(BigDecimal basePrice, BigDecimal comparePrice) {
        if (isLessThan(basePrice, comparePrice)) {
            throw new IllegalArgumentException("basePrice가 comparePrice보다 커야 합니다. basePrice: "
                    + basePrice + ", comparePrice: " + comparePrice);
        }

        return basePrice.subtract(comparePrice)
                .divide(basePrice, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateIncreaseRate(BigDecimal basePrice, BigDecimal comparePrice) {
        if (isLessThan(comparePrice, basePrice)) {
            throw new IllegalArgumentException("basePrice가 comparePrice보다 작아야 합니다. basePrice: "
                    + basePrice + ", comparePrice: " + comparePrice);
        }

        return comparePrice.subtract(basePrice)
                .divide(basePrice, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
