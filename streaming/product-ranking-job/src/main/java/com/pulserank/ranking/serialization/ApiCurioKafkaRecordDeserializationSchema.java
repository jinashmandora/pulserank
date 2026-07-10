package com.pulserank.ranking.serialization;

import com.pulserank.ranking.config.JobConfiguration;
import io.apicurio.registry.serde.avro.AvroKafkaDeserializer;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema;
import org.apache.flink.util.Collector;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;

public class ApiCurioKafkaRecordDeserializationSchema<T> implements KafkaRecordDeserializationSchema<T> {
    private final TypeInformation<T> producedType;

    private transient AvroKafkaDeserializer<T> avroKafkaDeserializer;

    public ApiCurioKafkaRecordDeserializationSchema(
            Class<T> targetClass
    ) {
        this.producedType = TypeInformation.of(targetClass);
    }

    @Override
    public void open(DeserializationSchema.InitializationContext context) throws Exception {
        avroKafkaDeserializer = new AvroKafkaDeserializer<>();
        avroKafkaDeserializer.configure(JobConfiguration.apicurioConfig(), false);
    }

    @Override
    public void deserialize(ConsumerRecord<byte[], byte[]> record, Collector<T> out) throws IOException {
        T value = avroKafkaDeserializer.deserialize(
                record.topic(),
                record.headers(),
                record.value()
        );
        if (value != null) {
            out.collect(value);
        }
    }

    @Override
    public TypeInformation<T> getProducedType() {
        return producedType;
    }
}
