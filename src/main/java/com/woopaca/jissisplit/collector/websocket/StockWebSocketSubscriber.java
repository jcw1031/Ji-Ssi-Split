package com.woopaca.jissisplit.collector.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woopaca.jissisplit.collector.websocket.dto.KISStockSubscription;
import com.woopaca.jissisplit.kis.KISAuthenticator;
import com.woopaca.jissisplit.stock.Stock;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class StockWebSocketSubscriber {

    private final WebSocketClient webSocketClient;
    private final WebSocketHandler webSocketHandler;
    private final KISAuthenticator kisAuthenticator;
    private final ObjectMapper objectMapper;

    private WebSocketSession webSocketSession;
    private Map<Stock, Boolean> subscribedStocks;

    public StockWebSocketSubscriber(WebSocketClient webSocketClient, WebSocketHandler webSocketHandler, KISAuthenticator kisAuthenticator, ObjectMapper objectMapper) {
        this.webSocketClient = webSocketClient;
        this.webSocketHandler = webSocketHandler;
        this.kisAuthenticator = kisAuthenticator;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() {
        webSocketSession = webSocketClient.execute(webSocketHandler, "ws://ops.koreainvestment.com:21000/tryitout/HDFSCNT0")
                .join();
        subscribedStocks = new ConcurrentHashMap<>();
    }

    @Async
    public void subscribe(Stock stock) {
        subscribedStocks.computeIfAbsent(stock, key -> {
            String webSocketApprovalKey = kisAuthenticator.getWebSocketApprovalKey();
            KISStockSubscription kisStockSubscription = KISStockSubscription
                    .subscribeNasdaq(stock.getCode(), webSocketApprovalKey);
            try {
                String payload = objectMapper.writeValueAsString(kisStockSubscription);
                log.info("payload: {}", payload);
                webSocketSession.sendMessage(new TextMessage(payload));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return true;
        });
    }

    @Async
    public void unsubscribe(Stock stock) {
        subscribedStocks.computeIfPresent(stock, (key, value) -> {
            String webSocketApprovalKey = kisAuthenticator.getWebSocketApprovalKey();
            KISStockSubscription kisStockUnsubscription = KISStockSubscription
                    .unsubscribeNasdaq(stock.getCode(), webSocketApprovalKey);
            try {
                String payload = objectMapper.writeValueAsString(kisStockUnsubscription);
                log.info("payload: {}", payload);
                webSocketSession.sendMessage(new TextMessage(payload));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return null;
        });
    }
}
