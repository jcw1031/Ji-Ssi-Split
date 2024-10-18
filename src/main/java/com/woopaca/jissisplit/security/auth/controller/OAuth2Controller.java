package com.woopaca.jissisplit.security.auth.controller;

import com.woopaca.jissisplit.common.ApiResults;
import com.woopaca.jissisplit.security.auth.dto.response.TokensResponse;
import com.woopaca.jissisplit.security.auth.model.Tokens;
import com.woopaca.jissisplit.security.auth.oauth2.KakaoUser;
import com.woopaca.jissisplit.security.auth.service.AuthService;
import com.woopaca.jissisplit.security.auth.service.KakaoOAuth2Service;
import com.woopaca.jissisplit.security.auth.utils.RefreshTokenCookies;
import com.woopaca.jissisplit.user.User;
import com.woopaca.jissisplit.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/oauth2")
@RestController
public class OAuth2Controller {

    private final KakaoOAuth2Service kakaoOAuth2Service;
    private final UserService userService;
    private final AuthService authService;

    public OAuth2Controller(KakaoOAuth2Service kakaoOAuth2Service, UserService userService, AuthService authService) {
        this.kakaoOAuth2Service = kakaoOAuth2Service;
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping("/kakao")
    public ApiResults.ApiResponse<TokensResponse> kakaoLogin(@RequestParam("code") String code, HttpServletResponse response) {
        KakaoUser kakaoUser = kakaoOAuth2Service.authenticate(code);
        User user = userService.registerOAuth2User(kakaoUser);
        Tokens tokens = authService.issueTokensFor(user);

        Cookie refreshTokenCookie = RefreshTokenCookies.generateCookie(tokens.refreshToken());
        response.addCookie(refreshTokenCookie);

        return ApiResults.success(new TokensResponse(tokens.accessToken()));
    }
}
