package com.document.recommendation.infra.exception;

import java.util.Map;

public class InvalidDocumentFormatException extends GeneralException {

    public InvalidDocumentFormatException(String message) {
        super(message);
    }

    public InvalidDocumentFormatException(Throwable cause) {
        super(cause);
    }

    public InvalidDocumentFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidDocumentFormatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public InvalidDocumentFormatException(Map<String, String> errorMsgs, String message, Throwable cause) {
        super(errorMsgs, message, cause);
    }

    public InvalidDocumentFormatException(Map<String, String> errorMsgs, String message) {
        super(errorMsgs, message);
    }
}
