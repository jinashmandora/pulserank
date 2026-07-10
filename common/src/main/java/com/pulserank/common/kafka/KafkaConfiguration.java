package com.pulserank.common.kafka;

public final class KafkaConfiguration {

    public static final String KAFKA_BOOTSTRAP_SERVERS = "kafka:29092";
    public static final String APICURIO_REGISTRY_URL = "http://apicurio-api:8080/apis/registry/v3";
    public static final String APICURIO_REGISTRY_LOCAL_URL = "http://localhost:8084/apis/registry/v3";


    private KafkaConfiguration() {
    }
}
