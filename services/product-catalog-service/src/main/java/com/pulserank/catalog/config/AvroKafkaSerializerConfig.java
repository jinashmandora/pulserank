package com.pulserank.catalog.config;

import io.apicurio.registry.serde.avro.AvroKafkaSerializer;
import org.apache.avro.specific.SpecificRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

import static com.pulserank.common.kafka.KafkaConfiguration.APICURIO_REGISTRY_LOCAL_URL;

@Configuration
public class AvroKafkaSerializerConfig {

    @Bean
    public AvroKafkaSerializer<SpecificRecord> catalogEventSerializer() {
        try (var serializer = new AvroKafkaSerializer<SpecificRecord>()) {
            serializer.configure(
                    Map.of(
                            "apicurio.registry.url", APICURIO_REGISTRY_LOCAL_URL,
                            "apicurio.registry.auto-register", true,
                            "apicurio.registry.find-latest", true
                    ),
                    false);
            return serializer;
        }
    }
}
