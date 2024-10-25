package com.woopaca.jissisplit.collector.websocket;

import com.woopaca.jissisplit.collector.StockPricePublisher;
import com.woopaca.jissisplit.collector.websocket.dto.StockPriceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Slf4j
@Component
public class StockPriceWebSocketHandler extends TextWebSocketHandler {

    private final RealTimeStockPriceMessageParser realTimeStockPriceMessageParser;
    private final StockPricePublisher stockPricePublisher;

    public StockPriceWebSocketHandler(RealTimeStockPriceMessageParser realTimeStockPriceMessageParser, StockPricePublisher stockPricePublisher) {
        this.realTimeStockPriceMessageParser = realTimeStockPriceMessageParser;
        this.stockPricePublisher = stockPricePublisher;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();
        if (payload.contains("PINGPONG")) {
            handlePingPong(session, message);
            return;
        }

        if (!payload.startsWith("0|HDFSCNT0")) {
            log.info("payload: {}", payload);
            return;
        }
        StockPriceResponse response = realTimeStockPriceMessageParser.parse(payload);
        stockPricePublisher.publish(response);
    }

    private void handlePingPong(WebSocketSession session, TextMessage message) {
        try {
            session.sendMessage(message);
        } catch (IOException e) {
            log.error("PONG 전송 실패", e);
        }
    }
}
