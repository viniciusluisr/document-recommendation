package com.document.recommendation.test.support;


import com.document.recommendation.domain.model.entity.ScoredDocument;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

public class TestApiEndpoints {

    private static final String BASE_URL = "http://127.0.0.1:8080/";
    private static final String VIEW_DOCUMENT_ENDPOINT = "/view";
    private static final String SIMILAR_DOCUMENTS_ENDPOINT = "/similar";


    public static ResponseEntity<HttpStatus> viewDocument(final String document, final String user) {

        return new TestRestTemplate().postForEntity(BASE_URL + document + VIEW_DOCUMENT_ENDPOINT, user, HttpStatus.class);
    }

    public static ResponseEntity<ScoredDocument> similar(final String document) {
        return new TestRestTemplate().getForEntity(BASE_URL + document + SIMILAR_DOCUMENTS_ENDPOINT, ScoredDocument.class);
    }

    public static void deleteAll() {
        new TestRestTemplate().delete(BASE_URL, Collections.EMPTY_MAP);
    }

}