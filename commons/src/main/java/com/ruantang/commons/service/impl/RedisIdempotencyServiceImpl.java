package com.ruantang.commons.service.impl;

import com.ruantang.commons.service.IdempotencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

// Redis实现（推荐）
@Service
@RequiredArgsConstructor
public class RedisIdempotencyServiceImpl implements IdempotencyService {

    private static final String IDEMPOTENCY_KEY_PREFIX = "idemp:";
    private final RedisTemplate<String, String> redisTemplate;
    @Override
    public boolean isProcessed(String messageId) {
        String key = buildKey(messageId);
        // 使用SETNX原子操作判断是否存在
        Boolean result = redisTemplate.opsForValue().setIfAbsent(key, "1", 30, TimeUnit.DAYS);
        return result != null && !result;
    }

    @Override
    public void markAsProcessed(String messageId) {
        // 标记处理成功（延长过期时间）
        String key = buildKey(messageId);
        redisTemplate.expire(key, 30, TimeUnit.DAYS);
    }

    private String buildKey(String messageId) {
        return IDEMPOTENCY_KEY_PREFIX + messageId;
    }
}
