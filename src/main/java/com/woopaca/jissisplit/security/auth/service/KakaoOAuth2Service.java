package com.woopaca.jissisplit.security.auth.service;

import com.woopaca.jissisplit.security.auth.oauth2.KakaoOAuth2Client;
import com.woopaca.jissisplit.security.auth.oauth2.KakaoToken;
import com.woopaca.jissisplit.security.auth.oauth2.KakaoUser;
import org.springframework.stereotype.Service;

@Service
public class KakaoOAuth2Service {

    private final KakaoOAuth2Client oAuth2Client;

    public KakaoOAuth2Service(KakaoOAuth2Client oAuth2Client) {
        this.oAuth2Client = oAuth2Client;
    }

    public KakaoUser authenticate(String code) {
        KakaoToken kakaoToken = oAuth2Client.requestToken(code);
        return oAuth2Client.requestUserInfo(kakaoToken.accessToken());
    }
}
