package com.woopaca.jissisplit.redis;

import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Profile("!test")
@Repository
public class RedisRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Async
    public void set(String key, String value) {
        Assert.notNull(key, "key는 null일 수 없습니다.");
        Assert.notNull(value, "value는 null일 수 없습니다.");
        redisTemplate.opsForValue().set(key, value);
    }

    @Async
    public void set(String key, String value, Duration duration) {
        Assert.notNull(key, "key는 null일 수 없습니다.");
        Assert.notNull(value, "value는 null일 수 없습니다.");
        redisTemplate.opsForValue().set(key, value, duration);
    }

    public String get(String key) {
        Assert.notNull(key, "key는 null일 수 없습니다.");
        return redisTemplate.opsForValue().get(key);
    }

    @Async
    public void remove(String key) {
        Assert.notNull(key, "key는 null일 수 없습니다.");
        redisTemplate.delete(key);
    }

    public Map<String, String> getAll(String pattern) {
        Set<String> keys = redisTemplate.opsForValue()
                .getOperations()
                .keys(pattern);
        if (Objects.isNull(keys)) {
            return Collections.emptyMap();
        }
        return keys.stream()
                .collect(Collectors.toMap(key -> key, key -> {
                    String value = redisTemplate.opsForValue().get(key);
                    return Objects.requireNonNullElse(value, "");
                }));
    }
}
