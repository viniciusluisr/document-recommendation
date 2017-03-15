package com.document.recommendation.application.api.v1.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

@Data
public class UserRequest implements Serializable {

    private String user;

    @JsonCreator
    public UserRequest(@JsonProperty("user") String user) {
        this.user = user;
    }
}
