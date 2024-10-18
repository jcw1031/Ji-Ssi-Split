package com.woopaca.jissisplit.user;

import com.woopaca.jissisplit.security.auth.oauth2.KakaoUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String name;

    private LocalDateTime registeredAt;

    private String appKey;

    private String appSecret;

    @Column(columnDefinition = "CHAR(8)")
    private String accountNumber;

    @Column(columnDefinition = "CHAR(2)")
    private String accountProductCode;

    private BigDecimal balance;

    public User() {
    }

    @Builder
    public User(String email, String name, LocalDateTime registeredAt, String appKey, String appSecret) {
        this.email = email;
        this.name = name;
        this.registeredAt = registeredAt;
        this.appKey = appKey;
        this.appSecret = appSecret;
    }

    public static User fromOAuth2User(KakaoUser kakaoUser) {
        return User.builder()
                .email(kakaoUser.email())
                .name(kakaoUser.nickname())
                .registeredAt(LocalDateTime.now())
                .build();
    }
}
