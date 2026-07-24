package com.pulserank.eventservice.repository;

import com.pulserank.common.cache.model.ProductCache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ProductCacheRepositoryImpl implements ProductCacheRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    public ProductCacheRepositoryImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Optional<ProductCache> get(Long productId) {
        return Optional.ofNullable(
                (ProductCache) redisTemplate.opsForValue()
                        .get(key(productId)));
    }

    private String key(Long productId) {
        return "product:" + productId;
    }
}
