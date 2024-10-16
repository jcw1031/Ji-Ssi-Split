package com.woopaca.jissisplit.security.auth.utils;

import com.woopaca.jissisplit.security.token.RefreshTokenProvider;
import jakarta.servlet.http.Cookie;

public final class RefreshTokenCookies {

    private RefreshTokenCookies() {
    }

    public static Cookie generateCookie(String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge((int) RefreshTokenProvider.DEFAULT_VALID_DURATION.toSeconds());
        return refreshTokenCookie;
    }
}
