package com.woopaca.jissisplit.processor;

import com.woopaca.jissisplit.stock.Stock;
import com.woopaca.jissisplit.user.User;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class StockTradeProcessorRegisterer {

    private final RedisMessageListenerContainer messageListenerContainer;
    private final ApplicationContext applicationContext;

    private final Map<InvestmentKey, MessageListener> messageListeners;

    public StockTradeProcessorRegisterer(RedisMessageListenerContainer messageListenerContainer, ApplicationContext applicationContext) {
        this.messageListenerContainer = messageListenerContainer;
        this.applicationContext = applicationContext;
        this.messageListeners = new ConcurrentHashMap<>();
    }

    @PreDestroy
    public void destroy() {
        messageListenerContainer.stop();
    }

    public void registerStockTrade(Stock stock, User user) {
        messageListeners.computeIfAbsent(new InvestmentKey(stock.getCode(), user.getId()), key -> {
            StockTradeProcessor messageListener = applicationContext.getBean(StockTradeProcessor.class);
            messageListenerContainer.addMessageListener(messageListener, new ChannelTopic(stock.getCode()));
            return messageListener;
        });
    }

    public void removeStockTrade(Stock stock, User user) {
        messageListeners.computeIfPresent(new InvestmentKey(stock.getCode(), user.getId()), (key, messageListener) -> {
            messageListenerContainer.removeMessageListener(messageListener);
            return null;
        });
    }

    record InvestmentKey(String stockCode, Long userId) {
    }
}
