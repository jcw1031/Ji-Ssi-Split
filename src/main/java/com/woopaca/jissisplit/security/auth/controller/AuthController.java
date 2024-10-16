package com.woopaca.jissisplit.security.auth.controller;

import com.woopaca.jissisplit.common.ApiResults;
import com.woopaca.jissisplit.security.auth.dto.response.TokensResponse;
import com.woopaca.jissisplit.security.auth.model.Tokens;
import com.woopaca.jissisplit.security.auth.service.AuthService;
import com.woopaca.jissisplit.security.auth.utils.RefreshTokenCookies;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/auth")
@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/tokens")
    public ApiResults.ApiResponse<TokensResponse> reissueTokens(@CookieValue("refreshToken") String refreshToken, HttpServletResponse response) {
        Tokens tokens = authService.reissueTokensBy(refreshToken);

        Cookie refreshTokenCookie = RefreshTokenCookies.generateCookie(tokens.refreshToken());
        response.addCookie(refreshTokenCookie);

        return ApiResults.success(new TokensResponse(tokens.accessToken()));
    }
}
