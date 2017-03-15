package com.document.recommendation.application.service;

import com.document.recommendation.domain.event.UnscoredDocumentsEvent;
import com.document.recommendation.domain.model.entity.ScoredDocument;

public interface ScoreDocumentsService {

    void score(final UnscoredDocumentsEvent event);
    ScoredDocument findByUrl(final String url);
    void deleteAll();

}