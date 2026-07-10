package com.pulserank.ranking.factory;

import com.clickhouse.data.ClickHouseFormat;
import com.pulserank.ranking.mapper.ProductScoreMapper;
import com.pulserank.schema.event.ProductScoreEvent;
import org.apache.flink.connector.clickhouse.convertor.ClickHouseConvertor;
import org.apache.flink.connector.clickhouse.sink.ClickHouseAsyncSink;
import org.apache.flink.connector.clickhouse.sink.ClickHouseClientConfig;

import static com.pulserank.common.clickhouse.ClickHouseConfiguration.*;

public final class ClickHouseSinkFactory {

    private static final int MAX_BATCH_SIZE = 5000;
    private static final int MAX_IN_FLIGHT_REQUESTS = 2;
    private static final int MAX_BUFFERED_REQUESTS = 20000;
    private static final long MAX_BATCH_SIZE_IN_BYTES = 5 * 1024 * 1024;
    private static final long MAX_TIME_IN_BUFFER_MS = 5 * 1000;
    private static final long MAX_RECORD_SIZE_IN_BYTES = 1000;

    private static final ClickHouseClientConfig clickHouseClientConfig = new ClickHouseClientConfig(
            CLICKHOUSE_URL,
            CLICKHOUSE_USER,
            CLICKHOUSE_PASSWORD,
            CLICKHOUSE_DATABASE,
            CLICKHOUSE_PRODUCT_SCORE_TABLENAME
    );

    private ClickHouseSinkFactory() {

    }

    public static ClickHouseAsyncSink<ProductScoreEvent> productScore() {

        ClickHouseConvertor<ProductScoreEvent> scoreConverter = new ClickHouseConvertor<>(
                ProductScoreEvent.class,
                new ProductScoreMapper()
        );

        return ClickHouseAsyncSink.<ProductScoreEvent>builder()
                .setMaxBatchSize(MAX_BATCH_SIZE)
                .setMaxInFlightRequests(MAX_IN_FLIGHT_REQUESTS)
                .setMaxBufferedRequests(MAX_BUFFERED_REQUESTS)
                .setMaxBatchSizeInBytes(MAX_BATCH_SIZE_IN_BYTES)
                .setMaxTimeInBufferMS(MAX_TIME_IN_BUFFER_MS)
                .setMaxRecordSizeInBytes(MAX_RECORD_SIZE_IN_BYTES)
                .setClickHouseClientConfig(clickHouseClientConfig)
                .setElementConverter(scoreConverter)
                .setClickHouseFormat(ClickHouseFormat.RowBinary)
                .build();
    }
}
