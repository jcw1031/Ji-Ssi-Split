package com.woopaca.jissisplit.kis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AccessTokenRequest(@JsonProperty("grant_type") String grantType,
                                 @JsonProperty("appkey") String appKey,
                                 @JsonProperty("appsecret") String appSecret) {

    public static AccessTokenRequest clientCredentials(String appKey, String appSecret) {
        return new AccessTokenRequest("client_credentials", appKey, appSecret);
    }
}