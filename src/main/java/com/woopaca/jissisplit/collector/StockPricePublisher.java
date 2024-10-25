package com.woopaca.jissisplit.collector;

import com.woopaca.jissisplit.collector.websocket.dto.StockPriceResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class StockPricePublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    public StockPricePublisher(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void publish(StockPriceResponse stockPriceResponse) {
        redisTemplate.convertAndSend(stockPriceResponse.stockCode(), stockPriceResponse);
    }
}
