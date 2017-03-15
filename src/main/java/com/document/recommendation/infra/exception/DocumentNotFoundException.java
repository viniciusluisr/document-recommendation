package com.document.recommendation.infra.exception;

import java.util.Map;

public class DocumentNotFoundException extends GeneralException {

    public DocumentNotFoundException(String message) {
        super(message);
    }

    public DocumentNotFoundException(Throwable cause) {
        super(cause);
    }

    public DocumentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public DocumentNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public DocumentNotFoundException(Map<String, String> errorMsgs, String message, Throwable cause) {
        super(errorMsgs, message, cause);
    }

    public DocumentNotFoundException(Map<String, String> errorMsgs, String message) {
        super(errorMsgs, message);
    }
}