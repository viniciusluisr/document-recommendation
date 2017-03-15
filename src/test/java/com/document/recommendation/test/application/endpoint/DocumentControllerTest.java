package com.document.recommendation.test.application.endpoint;

import com.document.recommendation.application.service.DocumentService;
import com.document.recommendation.domain.model.entity.ScoredDocument;
import com.document.recommendation.domain.model.to.DocumentTO;
import com.document.recommendation.infra.avro.AvroTemplates;
import com.document.recommendation.infra.service.DocumentServiceWrapper;
import com.document.recommendation.test.support.TestApiEndpoints;
import com.document.recommendation.test.support.TestFixtureSupport;
import com.document.recommendation.test.template.loader.AvroDocumentTemplateLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twitter.bijection.Injection;
import com.twitter.bijection.avro.GenericAvroCodecs;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment =  SpringBootTest.WebEnvironment.DEFINED_PORT)
public class DocumentControllerTest extends TestFixtureSupport {

    private static final Schema.Parser parser = new Schema.Parser();
    private static Schema schema = parser.parse(AvroTemplates.DOC_SCHEMA);
    private static final JavaSparkContext sc;
    private static final Injection<GenericRecord, byte[]> recordInjection = GenericAvroCodecs.toBinary(schema);
    private static final List<byte[]> docs = AvroDocumentTemplateLoader.getGenericValue();

    static {
        final SparkConf conf = new SparkConf()
                .setAppName("documents-streaming")
                .setMaster("local[2]");
        sc = new JavaSparkContext(conf);
    }

    @Autowired
    private DocumentService documentService;

    public void setUp() {
    }


    @Test
    public void recommendDocumentsTest() throws InterruptedException {

        DocumentServiceWrapper.setService(documentService);

        final JavaStreamingContext ssc = new JavaStreamingContext(sc, new Duration(1000));
        final ObjectMapper mapper = new ObjectMapper();
        JavaRDD<byte[]> data = ssc.sparkContext().parallelize(docs);
        Queue<JavaRDD<byte[]>> queue = new LinkedList<>();
        queue.add(data);

        JavaDStream<byte[]> inputStream = ssc.queueStream(queue);

        inputStream
                .map(message -> recordInjection.invert(message).get())
                .foreachRDD(rdd -> {
                    rdd.foreach(doc -> {
                        DocumentTO to = mapper.readValue(doc.toString(), DocumentTO.class);
                        DocumentServiceWrapper.getService().viewDocument(to);
                    });
                });

        ssc.start();
        ssc.awaitTerminationOrTimeout(6000);

        ResponseEntity<ScoredDocument> resp1 = TestApiEndpoints.similar("www.doc1.com");
        assertEquals(HttpStatus.OK, resp1.getStatusCode());

        ResponseEntity<ScoredDocument> resp2 = TestApiEndpoints.similar("www.doc2.com");
        assertEquals(HttpStatus.OK, resp2.getStatusCode());

        ResponseEntity<ScoredDocument> resp3 = TestApiEndpoints.similar("www.doc3.com");
        assertEquals(HttpStatus.OK, resp3.getStatusCode());

        ResponseEntity<ScoredDocument> resp4 = TestApiEndpoints.similar("www.doc4.com");
        assertEquals(HttpStatus.OK, resp4.getStatusCode());

        ResponseEntity<ScoredDocument> resp5 = TestApiEndpoints.similar("www.doc5.com");
        assertEquals(HttpStatus.OK, resp5.getStatusCode());

        ResponseEntity<ScoredDocument> resp6 = TestApiEndpoints.similar("www.doc6.com");
        assertEquals(HttpStatus.OK, resp6.getStatusCode());

        TestApiEndpoints.deleteAll();

    }

}