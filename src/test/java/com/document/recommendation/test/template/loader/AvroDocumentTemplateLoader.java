package com.document.recommendation.test.template.loader;

import br.com.six2six.fixturefactory.Fixture;
import com.document.recommendation.domain.model.entity.Document;
import com.document.recommendation.infra.avro.AvroTemplates;
import com.document.recommendation.test.support.TestFixtureSupport;
import com.twitter.bijection.Injection;
import com.twitter.bijection.avro.GenericAvroCodecs;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;

import java.util.ArrayList;
import java.util.List;

public class AvroDocumentTemplateLoader extends TestFixtureSupport {

    @Override
    public void setUp() {

    }

    public static List<byte[]> getGenericValue() {

        Schema.Parser parser = new Schema.Parser();
        Schema schema = parser.parse(AvroTemplates.DOC_SCHEMA);
        Injection<GenericRecord, byte[]> recordInjection = GenericAvroCodecs.toBinary(schema);

        final List<byte[]> records = new ArrayList<>();
        final List<Document> docs = Fixture.from(Document.class).gimme(6, "valid");

        docs.forEach(d -> {
            GenericData.Record avroRecord = new GenericData.Record(schema);
            avroRecord.put("url",  d.getUrl());
            avroRecord.put("user", d.getUsers().get(0));
            byte[] bytes = recordInjection.apply(avroRecord);
            records.add(bytes);
        });

        return records;
    }
}
