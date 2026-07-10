CREATE MATERIALIZED VIEW mv_product_score
ENGINE = ReplacingMergeTree(updated_at)
ORDER BY (category_id, score, product_id)
AS
SELECT
    product_id,
    category_id,
    score,
    updated_at
FROM product_score;