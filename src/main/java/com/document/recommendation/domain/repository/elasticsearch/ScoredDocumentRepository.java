package com.document.recommendation.domain.repository.elasticsearch;

import com.document.recommendation.domain.model.entity.ScoredDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.concurrent.Future;

@Repository
public interface ScoredDocumentRepository extends ElasticsearchRepository<ScoredDocument, String> {

}
