package com.pulserank.catalog.mapper;

import com.pulserank.catalog.enity.Product;
import com.pulserank.catalog.model.ProductResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getSku(),
                product.getDescription(),
                product.getCategory().getId(),
                product.getBrand(),
                product.getPrice(),
                product.getCurrency()
        );
    }
}
