package com.pulserank.catalog.enity;

import com.pulserank.catalog.enums.OperationType;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "products_outbox")
public class ProductsOutbox {

    @Id
    private UUID id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation", nullable = false)
    private OperationType operation;

    @Column(name = "payload")
    private byte[] payload;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public ProductsOutbox(UUID id,
                          Long productId,
                          OperationType operation,
                          byte[] payload,
                          Instant createdAt) {
        this.id = id;
        this.productId = productId;
        this.operation = operation;
        this.payload = payload;
        this.createdAt = createdAt;
    }

    protected ProductsOutbox() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public OperationType getOperation() {
        return operation;
    }

    public void setOperation(OperationType operation) {
        this.operation = operation;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
