package com.document.recommendation.domain.model.entity;


import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.List;

@Value
@org.springframework.data.mongodb.core.mapping.Document
public class Document implements Serializable {

    @Id
    private String url;
    private List<String> users;

}