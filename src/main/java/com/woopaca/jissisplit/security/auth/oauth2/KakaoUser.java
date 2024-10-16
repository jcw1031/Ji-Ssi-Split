package com.woopaca.jissisplit.security.auth.oauth2;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.util.StringUtils;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoUser(KakaoAccount kakaoAccount) {

    public String email() {
        return kakaoAccount.email;
    }

    public String profileImageUrl() {
        if (!StringUtils.hasText(kakaoAccount.profile.profileImageUrl)
                || kakaoAccount.profile.profileImageUrl.contains("/account_images/default_profile")) {
            return null;
        }
        return kakaoAccount.profile.profileImageUrl;
    }

    public String nickname() {
        return kakaoAccount.profile.nickname;
    }

    public String provider() {
        return "KAKAO";
    }

    record KakaoAccount(Profile profile, String email) {
    }

    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    record Profile(String profileImageUrl, String nickname) {
    }
}
