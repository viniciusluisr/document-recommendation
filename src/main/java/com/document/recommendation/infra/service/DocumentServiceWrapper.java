package com.document.recommendation.infra.service;

import com.document.recommendation.application.service.DocumentService;
import com.document.recommendation.infra.service.provider.KafkaServiceProvider;

public class DocumentServiceWrapper {

    private static DocumentService service;

    public static DocumentService getService() {
        return service;
    }

    public static void setService(DocumentService service) {
        DocumentServiceWrapper.service = service;
    }
}
