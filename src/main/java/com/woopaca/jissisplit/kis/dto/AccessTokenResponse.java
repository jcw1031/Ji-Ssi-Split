package com.woopaca.jissisplit.kis.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.time.LocalDateTime;

public record AccessTokenResponse(@JsonAlias("access_token") String accessToken,
                                  @JsonAlias("access_token_token_expired") LocalDateTime accessTokenExpired,
                                  @JsonAlias("token_type") String tokenType,
                                  @JsonAlias("expires_in") long expiresIn) {
}