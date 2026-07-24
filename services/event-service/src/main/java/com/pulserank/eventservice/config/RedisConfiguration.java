package com.pulserank.eventservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;

@Configuration
public class RedisConfiguration {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory connectionFactory,
            GenericJacksonJsonRedisSerializer valueSerializer
    ) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        StringRedisSerializer keySerializer = new StringRedisSerializer();


        template.setKeySerializer(keySerializer);
        template.setValueSerializer(valueSerializer);
        template.setHashKeySerializer(keySerializer);
        template.setHashValueSerializer(valueSerializer);

        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public GenericJacksonJsonRedisSerializer valueSerializer() {
        return GenericJacksonJsonRedisSerializer.builder()
                .customize(mapperBuilder -> {
                    mapperBuilder.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
                    mapperBuilder.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                    mapperBuilder.findAndAddModules();
                })
                .enableDefaultTyping(BasicPolymorphicTypeValidator.builder()
                        .allowIfBaseType("com.pulserank.cache.model")
                        .allowIfBaseType("java.math.BigDecimal")
                        .build())
                .build();
    }
}
