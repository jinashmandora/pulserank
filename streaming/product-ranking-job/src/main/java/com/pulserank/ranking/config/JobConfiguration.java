package com.pulserank.ranking.config;

import com.pulserank.common.kafka.KafkaConfiguration;

import java.util.Map;

public final class JobConfiguration {

    public static final String JOB_NAME = "PulseRank Ranking Job";

    private static final String CG_PREFIX = "flink.ranking.";
    private static final String CG_SUFFIX = ".cg";

    public static final String CG_PRODUCT_INTERACTIONS = CG_PREFIX + "product-interactions" + CG_SUFFIX;
    public static final String CG_PRODUCT_CHANGES = CG_PREFIX + "product-changes" + CG_SUFFIX;

    private JobConfiguration() {
    }

    public static Map<String, Object> apicurioConfig() {
        return Map.of(
                "apicurio.registry.url", KafkaConfiguration.APICURIO_REGISTRY_URL,
                "apicurio.registry.use-specific-avro-reader", true,
                "apicurio.registry.auto-register", true
        );
    }
}