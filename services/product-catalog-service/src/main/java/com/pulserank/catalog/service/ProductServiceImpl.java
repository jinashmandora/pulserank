package com.pulserank.catalog.service;

import com.pulserank.catalog.enity.Category;
import com.pulserank.catalog.enity.Product;
import com.pulserank.catalog.enums.OperationType;
import com.pulserank.catalog.exception.CategoryNotFoundException;
import com.pulserank.catalog.exception.DuplicateProductException;
import com.pulserank.catalog.exception.ProductNotFoundException;
import com.pulserank.catalog.mapper.ProductMapper;
import com.pulserank.catalog.model.CreateProductRequest;
import com.pulserank.catalog.model.ProductOutboxEvent;
import com.pulserank.catalog.model.ProductResponse;
import com.pulserank.catalog.repository.CategoryRepository;
import com.pulserank.catalog.repository.ProductRepository;
import com.pulserank.schema.event.Operation;
import com.pulserank.schema.event.ProductChangeEvent;
import com.pulserank.schema.event.ProductDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OutboxEventPublisher outboxEventPublisher;
    private final ProductMapper productMapper;

    public ProductServiceImpl(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            OutboxEventPublisher outboxEventPublisher,
            ProductMapper productMapper
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.outboxEventPublisher = outboxEventPublisher;
        this.productMapper = productMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getProducts() {
        return productRepository.findByActiveTrueOrderByName()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProduct(Long id) {
        return productRepository.
                findByIdAndActiveTrue(id)
                .map(productMapper::toResponse)
                .orElseThrow(ProductNotFoundException::new);
    }

    @Override
    public ProductResponse save(CreateProductRequest request) {
        try {
            Category category = categoryRepository.
                    findByIdAndActiveTrue(request.categoryId())
                    .orElseThrow(CategoryNotFoundException::new);

            Product product = new Product(
                    request.name(),
                    request.sku(),
                    request.description(),
                    category,
                    request.brand(),
                    request.price(),
                    request.currency()
            );
            Product saved = productRepository.save(product);

            ProductOutboxEvent upsertEvent = new ProductOutboxEvent(
                    saved.getId(),
                    OperationType.UPSERT,
                    (eventId, eventTime) -> ProductChangeEvent.newBuilder()
                            .setEventId(eventId)
                            .setProductId(saved.getId())
                            .setEventTime(eventTime)
                            .setOperation(Operation.UPSERT)
                            .setProductDetails(ProductDetails.newBuilder()
                                    .setCategoryId(saved.getCategory().getId())
                                    .setName(saved.getName())
                                    .setBrand(saved.getBrand())
                                    .setPrice(saved.getPrice())
                                    .setCurrency(saved.getCurrency())
                                    .build())
                            .build()
            );
            outboxEventPublisher.publish(upsertEvent);
            return productMapper.toResponse(saved);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateProductException(request.sku());
        }
    }

    @Override
    public void delete(Long productId) {
        ProductOutboxEvent deleteEvent = new ProductOutboxEvent(
                productId,
                OperationType.DELETE,
                (eventId, eventTime) -> ProductChangeEvent.newBuilder()
                        .setEventId(eventId)
                        .setEventTime(eventTime)
                        .setOperation(Operation.DELETE)
                        .setProductDetails(null)
                        .build()
        );
        productRepository.deleteById(productId);
        outboxEventPublisher.publish(deleteEvent);
    }

}
