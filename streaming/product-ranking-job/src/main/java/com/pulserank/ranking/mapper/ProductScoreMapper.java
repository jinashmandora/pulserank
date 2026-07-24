package com.pulserank.ranking.mapper;

import com.clickhouse.data.ClickHouseColumn;
import com.clickhouse.data.ClickHouseDataType;
import com.pulserank.schema.event.ProductScoreEvent;
import org.apache.flink.connector.clickhouse.convertor.ColumnBinding;
import org.apache.flink.connector.clickhouse.convertor.DataMapper;

import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

public class ProductScoreMapper extends DataMapper<ProductScoreEvent> {

    private static final String MAP_PRODUCT_ID = "productId";
    private static final String MAP_CATEGORY_ID = "categoryId";
    private static final String MAP_SCORE = "score";
    private static final String MAP_UPDATED_AT = "updatedAt";


    private static final String COL_PRODUCT_ID = "product_id";
    private static final String COL_CATEGORY_ID = "category_id";
    private static final String COL_SCORE = "score";
    private static final String COL_UPDATED_AT = "updated_at";


    @Override
    public void toMap(ProductScoreEvent productScoreEvent, Map<String, Object> map) {
        map.put(MAP_PRODUCT_ID, productScoreEvent.getProductId());
        map.put(MAP_CATEGORY_ID, productScoreEvent.getProductDetails().getCategoryId());
        map.put(MAP_SCORE, productScoreEvent.getScore());
        map.put(MAP_UPDATED_AT, productScoreEvent.getEventTime().atZone(ZoneOffset.UTC));
    }

    @Override
    public List<ColumnBinding> bindings() {
        return List.of(
                ColumnBinding.of(MAP_PRODUCT_ID, COL_PRODUCT_ID, ClickHouseColumn.of(ClickHouseDataType.UInt64)),
                ColumnBinding.of(MAP_CATEGORY_ID, COL_CATEGORY_ID, ClickHouseColumn.of(ClickHouseDataType.UInt64)),
                ColumnBinding.of(MAP_SCORE, COL_SCORE, ClickHouseColumn.of(ClickHouseDataType.UInt32)),
                ColumnBinding.dateTime64(MAP_UPDATED_AT, COL_UPDATED_AT, 3)
        );
    }
}
