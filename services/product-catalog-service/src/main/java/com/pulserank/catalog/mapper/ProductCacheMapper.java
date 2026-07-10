package com.pulserank.catalog.mapper;

import com.pulserank.catalog.enity.Product;
import com.pulserank.common.cache.model.ProductCache;
import org.springframework.stereotype.Component;

@Component
public class ProductCacheMapper {

    public ProductCache toCache(Product product) {
        return new ProductCache(
                product.getId(),
                product.getCategory().getId(),
                product.getSku(),
                product.getName(),
                product.getBrand(),
                product.getPrice(),
                product.getCurrency(),
                product.getActive()
        );
    }
}
