package com.pulserank.catalog.service;


import com.pulserank.catalog.model.CreateProductRequest;
import com.pulserank.catalog.model.ProductResponse;

import java.util.List;

public interface ProductService {

    List<ProductResponse> getProducts();

    ProductResponse getProduct(Long id);

    ProductResponse save(CreateProductRequest request);

    void delete(Long productId);
}
