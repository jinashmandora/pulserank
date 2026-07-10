CREATE TABLE products_outbox
(
    id         UUID PRIMARY KEY,
    product_id BIGINT      NOT NULL,
    operation  VARCHAR(32) NOT NULL,
    payload    BYTEA,
    created_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_products_outbox_created_at
    ON products_outbox (created_at);