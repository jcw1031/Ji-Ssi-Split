package com.woopaca.jissisplit.kis;

import com.woopaca.jissisplit.kis.dto.AccessTokenRequest;
import com.woopaca.jissisplit.kis.dto.AccessTokenResponse;
import com.woopaca.jissisplit.kis.dto.AvailablePurchaseAmountResponse;
import com.woopaca.jissisplit.kis.dto.WebSocketApprovalKeyRequest;
import com.woopaca.jissisplit.kis.dto.WebSocketApprovalKeyResponse;
import com.woopaca.jissisplit.stock.Stock;
import com.woopaca.jissisplit.user.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;

@Component
public class KISClient {

    private final RestClient restClient;
    private final KISProperties kisProperties;

    public KISClient(RestClient restClient, KISProperties kisProperties) {
        this.restClient = restClient;
        this.kisProperties = kisProperties;
    }

    public String requestAccessToken(User user) {
        String uri = kisProperties.getDomain() + "/oauth2/tokenP";
        AccessTokenResponse response = restClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(AccessTokenRequest.clientCredentials(user.getAppKey(), user.getAppSecret()))
                .retrieve()
                .body(AccessTokenResponse.class);
        assert response != null;
        return response.accessToken();
    }

    public String requestWebSocketApprovalKey(User user) {
        String uri = kisProperties.getDomain() + "/oauth2/Approval";
        WebSocketApprovalKeyResponse response = restClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(WebSocketApprovalKeyRequest.clientCredentials(user.getAppKey(), user.getAppSecret()))
                .retrieve()
                .body(WebSocketApprovalKeyResponse.class);
        assert response != null;
        return response.approvalKey();
    }

    public AvailablePurchaseAmountResponse requestAvailablePurchaseAmount(User user, Stock stock,
                                                                          BigDecimal stockPrice, String accessToken) {
        String uri = UriComponentsBuilder.fromUriString(kisProperties.getDomain() + "/uapi/overseas-stock/v1/trading/inquire-psamount")
                .queryParam("CANO", user.getAccountNumber())
                .queryParam("ACNT_PRDT_CD", user.getAccountProductCode())
                .queryParam("OVRS_EXCG_CD", "NASD")
                .queryParam("OVRS_ORD_UNPR", stockPrice)
                .queryParam("ITEM_CD", stock.getCode())
                .toUriString();

        return restClient.get()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .header("appkey", user.getAppKey())
                .header("appsecret", user.getAppSecret())
                .header("tr_id", "VTTS3007R")
                .retrieve()
                .body(AvailablePurchaseAmountResponse.class);
    }
}
