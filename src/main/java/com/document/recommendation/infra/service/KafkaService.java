package com.document.recommendation.infra.service;

import com.document.recommendation.domain.model.to.DocumentTO;

public interface KafkaService {

    void produce(final DocumentTO to);

}