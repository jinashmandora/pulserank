package com.pulserank.catalog.repository;

import com.pulserank.catalog.enity.ProductsOutbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OutboxRepository extends JpaRepository<ProductsOutbox, UUID> {

    @Query(
            value = """
                    SELECT * FROM outbox
                    ORDER BY created_at ASC
                    LIMIT :batchSize
                    FOR UPDATE SKIP LOCKED
                    """,
            nativeQuery = true
    )
    List<ProductsOutbox> findPendingBatch(@Param("batchSize") int batchSize);
}
