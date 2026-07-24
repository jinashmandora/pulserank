CREATE TABLE product_score
(
    product_id      UInt64,
    category_id     UInt64,
    score           UInt32,
    updated_at      DateTime64(3)
)
ENGINE = ReplacingMergeTree(updated_at)
ORDER BY product_id;