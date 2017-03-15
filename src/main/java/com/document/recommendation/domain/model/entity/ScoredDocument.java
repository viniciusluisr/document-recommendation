package com.document.recommendation.domain.model.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Value
@org.springframework.data.elasticsearch.annotations.Document(indexName = "documents", type = "scoredDocument")
public class ScoredDocument implements Serializable {

    @Id
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
    private String url;
    @Field(type = FieldType.Nested, index = FieldIndex.not_analyzed, store = true)
    private List<Score> scores;

}