package com.document.recommendation.infra.kafka;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Data
@Component
public class KafkaConfig {

    @Value("${kafka.bootstrap.servers}")
    private String bootstrapServers;
    @Value("${kafka.key.serialize}")
    private String keySerialize;
    @Value("${kafka.value.serializer}")
    private String valueSerializer;
    @Value("${kafka.topic}")
    private String topic;

    public Properties getKafkaConfiguration() {
        final Properties props = new Properties();
        props.put("bootstrap.servers", getBootstrapServers());
        props.put("key.serializer", getValueSerializer());
        props.put("value.serializer", getValueSerializer());
        return props;
    }

}