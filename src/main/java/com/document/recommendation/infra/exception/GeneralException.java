package com.document.recommendation.infra.exception;

import org.apache.commons.lang.RandomStringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.springframework.util.CollectionUtils.isEmpty;

public class GeneralException extends RuntimeException {

    private final Map<String, String> errorMsgs = new HashMap<>();

    public GeneralException(String message) {
        super(message);
        putUniqueErrorMessage(message);
    }

    public GeneralException(Throwable cause) {
        super(cause);
        this.errorMsgs.put(UUID.randomUUID().toString(), cause.getMessage());
    }

    public GeneralException(String message, Throwable cause) {
        super(message, cause);
        putUniqueErrorMessage(message);
    }

    public GeneralException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        putUniqueErrorMessage(message);
    }

    public GeneralException(Map<String, String> errorMsgs, String message, Throwable cause) {
        super(message, cause);
        if (!isEmpty(errorMsgs)) {
            this.errorMsgs.clear();
            this.errorMsgs.putAll(errorMsgs);
        }
    }

    public GeneralException(Map<String, String> errorMsgs, String message) {
        super(message);
        if (!isEmpty(errorMsgs)) {
            this.errorMsgs.clear();
            this.errorMsgs.putAll(errorMsgs);
        }
    }

    private void putUniqueErrorMessage(String message) {
        this.errorMsgs.put(RandomStringUtils.randomAlphabetic(6), message);
    }

    public Map<String, String> getErrors() {
        return Collections.unmodifiableMap(errorMsgs);
    }
}
