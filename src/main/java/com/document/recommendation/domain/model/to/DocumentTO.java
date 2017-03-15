package com.document.recommendation.domain.model.to;

import com.document.recommendation.infra.exception.InvalidDocumentFormatException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Value;
import org.glassfish.jersey.server.JSONP;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public class DocumentTO {

    private String url;
    private String user;

    @JsonCreator
    public DocumentTO(@JsonProperty("url") String url, @JsonProperty("user") String user) {
        this.url = Optional.ofNullable(url)
                .filter(i -> !i.equals("")).orElseThrow(() -> new InvalidDocumentFormatException("URL do documento informado inválida."));;
        this.user = Optional.ofNullable(user)
                .filter(i -> !i.equals("")).orElseThrow(() -> new InvalidDocumentFormatException("Usuário informado é inválido."));
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }
}
