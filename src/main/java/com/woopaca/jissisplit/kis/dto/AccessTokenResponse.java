package com.woopaca.jissisplit.kis.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public record AccessTokenResponse(@JsonAlias("access_token") String accessToken,
                                  @JsonAlias("token_type") String tokenType,
                                  @JsonAlias("expires_in") long expiresIn) {
}