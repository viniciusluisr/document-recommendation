package com.document.recommendation.application.service;

import com.document.recommendation.domain.model.to.DocumentTO;
import org.springframework.context.annotation.Scope;

import java.io.Serializable;

@Scope("singleton")
public interface DocumentService extends Serializable {

    void viewDocument(final DocumentTO document);
    void deleteAll();

}
