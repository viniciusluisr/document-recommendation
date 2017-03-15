package com.document.recommendation.domain.repository.mongodb;

import com.document.recommendation.domain.model.entity.Document;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public interface DocumentRepository extends MongoRepository<Document, String>, Serializable {

    Document findByUrl(final String url);
}
