package com.document.recommendation.infra.service.provider;

import com.document.recommendation.domain.model.to.DocumentTO;
import com.document.recommendation.infra.avro.AvroTemplates;
import com.document.recommendation.infra.kafka.KafkaConfig;
import com.document.recommendation.infra.service.KafkaService;
import com.twitter.bijection.Injection;
import com.twitter.bijection.avro.GenericAvroCodecs;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class KafkaServiceProvider implements KafkaService {

    private KafkaConfig kafkaConfig;
    private KafkaProducer<String, byte[]> producer;
    private static final Schema schema;
    private static final Injection<GenericRecord, byte[]> recordInjection;

    static {
        final Schema.Parser parser = new Schema.Parser();
        schema = parser.parse(AvroTemplates.DOC_SCHEMA);
        recordInjection = GenericAvroCodecs.toBinary(schema);
    }

    @Autowired
    public KafkaServiceProvider(KafkaConfig kafkaConfig) {
        this.kafkaConfig = kafkaConfig;
        this.producer =  new KafkaProducer<>(kafkaConfig.getKafkaConfiguration());
    }

    @Override
    public void produce(final DocumentTO to) {

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.submit(() -> {
                GenericData.Record avroRecord = new GenericData.Record(schema);
                avroRecord.put("url", to.getUrl());
                avroRecord.put("user",to.getUser());

                byte[] bytes = recordInjection.apply(avroRecord);

                ProducerRecord<String, byte[]> record = new ProducerRecord<>(kafkaConfig.getTopic(), bytes);
                producer.send(record);
        });

        executor.shutdown();

    }

}