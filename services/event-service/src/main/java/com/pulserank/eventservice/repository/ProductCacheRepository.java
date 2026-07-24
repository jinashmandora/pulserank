package com.pulserank.eventservice.repository;


import com.pulserank.common.cache.model.ProductCache;

import java.util.Optional;

public interface ProductCacheRepository {

    Optional<ProductCache> get(Long productId);
}
