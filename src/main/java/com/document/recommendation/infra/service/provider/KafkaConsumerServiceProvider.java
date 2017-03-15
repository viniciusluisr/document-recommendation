package com.document.recommendation.infra.service.provider;

import com.document.recommendation.application.service.DocumentService;
import com.document.recommendation.domain.model.to.DocumentTO;
import com.document.recommendation.infra.avro.AvroTemplates;
import com.document.recommendation.infra.kafka.KafkaConfig;
import com.document.recommendation.infra.service.DocumentServiceWrapper;
import com.document.recommendation.infra.service.KafkaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twitter.bijection.Injection;
import com.twitter.bijection.avro.GenericAvroCodecs;
import kafka.serializer.DefaultDecoder;
import kafka.serializer.StringDecoder;
import lombok.RequiredArgsConstructor;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.elasticsearch.common.inject.Inject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Profile("development")
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Component
public class KafkaConsumerServiceProvider implements CommandLineRunner {

    private static final Schema schema;
    private static final Injection<GenericRecord, byte[]> recordInjection;
    private static final JavaSparkContext sc;

    static {
        final Schema.Parser parser = new Schema.Parser();
        schema = parser.parse(AvroTemplates.DOC_SCHEMA);
        recordInjection = GenericAvroCodecs.toBinary(schema);
        final SparkConf conf = new SparkConf()
                .setAppName("documents-streaming")
                .setMaster("local[2]")
                .set("spark.driver.host", "localhost");
        sc = new JavaSparkContext(conf);
    }

    private KafkaConfig kafkaConfig;

    @Autowired
    public KafkaConsumerServiceProvider(DocumentService documentService, KafkaConfig kafkaConfig) {
        DocumentServiceWrapper.setService(documentService);
        this.kafkaConfig = kafkaConfig;
    }

    @Override
    public void run(String... strings) throws Exception {

        JavaStreamingContext ssc = new JavaStreamingContext(sc, new Duration(2000));

        Set<String> topics = Collections.singleton(kafkaConfig.getTopic());
        Map<String, String> kafkaParams = new HashMap<>();
        kafkaParams.put("metadata.broker.list", kafkaConfig.getBootstrapServers());

        JavaPairInputDStream<String, byte[]> directKafkaStream = KafkaUtils.createDirectStream(ssc,
                String.class, byte[].class, StringDecoder.class, DefaultDecoder.class, kafkaParams, topics);

        final ObjectMapper mapper = new ObjectMapper();
        directKafkaStream
                .map(message -> recordInjection.invert(message._2).get())
                .foreachRDD( rdd -> {
                    rdd.foreach(doc -> {
                        DocumentTO to = mapper.readValue(doc.toString(), DocumentTO.class);
                        DocumentServiceWrapper.getService().viewDocument(to);
                    });
                });

        ssc.start();
        ssc.awaitTermination();
    }
}