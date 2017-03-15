package com.document.recommendation.domain.model.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

@Value
public class Score implements Serializable {

    @Id
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
    private String url;
    @Field(type = FieldType.Double, index = FieldIndex.not_analyzed, store = true)
    private Double score;

}