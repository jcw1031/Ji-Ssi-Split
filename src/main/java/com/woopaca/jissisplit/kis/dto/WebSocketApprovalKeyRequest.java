package com.woopaca.jissisplit.kis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WebSocketApprovalKeyRequest(@JsonProperty("grant_type") String grantType,
                                          @JsonProperty("appkey") String appKey,
                                          @JsonProperty("secretkey") String appSecret) {

    public static WebSocketApprovalKeyRequest clientCredentials(String appKey, String appSecret) {
        return new WebSocketApprovalKeyRequest("client_credentials", appKey, appSecret);
    }
}