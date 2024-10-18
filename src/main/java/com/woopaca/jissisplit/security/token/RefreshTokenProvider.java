package com.woopaca.jissisplit.security.token;

import com.woopaca.jissisplit.redis.RedisRepository;
import com.woopaca.jissisplit.user.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Component
public class RefreshTokenProvider {

    public static final Duration DEFAULT_VALID_DURATION = Duration.ofDays(14);

    private final RedisRepository keyValueRepository;

    public RefreshTokenProvider(RedisRepository keyValueRepository) {
        this.keyValueRepository = keyValueRepository;
    }

    public String issueRefreshToken(User principal) {
        String refreshToken = generateRefreshToken();
        String key = generateKey(refreshToken);
        keyValueRepository.set(key, principal.getEmail(), DEFAULT_VALID_DURATION);
        return refreshToken;
    }

    /**
     * 주제를 꺼내고 refresh token 제거
     * @param refreshToken 유효한 refresh token
     * @return
     */
    public String takeOutSubject(String refreshToken) {
        String key = generateKey(refreshToken);
        String email = keyValueRepository.get(key);
        if (!StringUtils.hasText(email)) {
        }
        keyValueRepository.remove(key);
        return email;
    }

    /**
     * refresh token 만료 ({@code user}의 refresh token을 만료)
     * @param user {@code null}이 아닌 사용자
     */
    public void expireRefreshToken(User user) {
        String email = user.getEmail();
        keyValueRepository.getAll("refresh_token:*")
                .entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), email))
                .map(Map.Entry::getKey)
                .forEach(keyValueRepository::remove);
    }

    private String generateRefreshToken() {
        return UUID.randomUUID()
                .toString()
                .replaceAll("-", "");
    }

    private String generateKey(String refreshToken) {
        return String.join(":", "refresh_token", refreshToken);
    }
}
