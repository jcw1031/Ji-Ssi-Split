package com.woopaca.jissisplit.kis;

import com.woopaca.jissisplit.redis.RedisRepository;
import com.woopaca.jissisplit.user.User;
import com.woopaca.jissisplit.user.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class KISAuthenticator {

    private final KISClient kisClient;
    private final RedisRepository redisRepository;
    private final UserRepository userRepository;

    public KISAuthenticator(KISClient kisClient, RedisRepository redisRepository, UserRepository userRepository) {
        this.kisClient = kisClient;
        this.redisRepository = redisRepository;
        this.userRepository = userRepository;
    }

    public String getWebSocketApprovalKey() {
        User admin = userRepository.findAdmin();
        String key = generateKey(admin.getId(), "approval_key");
        String approvalKey = redisRepository.get(key);
        if (approvalKey != null) {
            return approvalKey;
        }
        String newApprovalKey = kisClient.requestWebSocketApprovalKey(admin);
        redisRepository.set(key, newApprovalKey, Duration.ofHours(24));
        return newApprovalKey;
    }

    public String getAccessToken(User user) {
        String key = generateKey(user.getId(), "access_token");
        String accessToken = redisRepository.get(key);
        if (accessToken != null) {
            return accessToken;
        }
        String newAccessToken = kisClient.requestAccessToken(user);
        redisRepository.set(key, newAccessToken, Duration.ofHours(24));
        return newAccessToken;
    }

    private String generateKey(Long userId, String type) {
        return String.join(":", "kis", type, String.valueOf(userId));
    }
}
