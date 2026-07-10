package com.pulserank.catalog.controller;

import com.pulserank.catalog.model.CreateProductRequest;
import com.pulserank.catalog.model.ProductResponse;
import com.pulserank.catalog.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getProducts() {
        return productService.getProducts();
    }

    @GetMapping(value = "/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse getProduct(@PathVariable("productId") Long productId) {
        return productService.getProduct(productId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse create(@RequestBody CreateProductRequest request) {
        return productService.save(request);
    }
}
