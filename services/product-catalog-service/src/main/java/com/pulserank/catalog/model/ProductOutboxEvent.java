package com.pulserank.catalog.model;

import com.pulserank.catalog.enums.OperationType;
import org.apache.avro.specific.SpecificRecord;

import java.time.Instant;
import java.util.function.BiFunction;

public record ProductOutboxEvent(
        Long productId,
        OperationType operationType,
        BiFunction<String, Instant, SpecificRecord> avroSchema
) {

}
