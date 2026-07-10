package com.pulserank.catalog.repository;

import com.pulserank.catalog.enity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByActiveTrueOrderByName();

    Optional<Product> findByIdAndActiveTrue(Long id);
}
