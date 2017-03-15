package com.document.recommendation.domain.event;

import com.document.recommendation.domain.model.entity.Document;
import org.springframework.context.ApplicationEvent;

public class UnscoredDocumentsEvent extends ApplicationEvent {

    private Iterable<Document> docs;

    public UnscoredDocumentsEvent(Iterable<Document> docs) {
        super(docs);
        this.docs = docs;
    }

    public Iterable<Document> getDocs() {
        return docs;
    }
}
