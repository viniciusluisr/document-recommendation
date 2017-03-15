package com.document.recommendation.application.api.v1.endpoint;


import com.document.recommendation.application.service.DocumentService;
import com.document.recommendation.application.service.ScoreDocumentsService;
import com.document.recommendation.domain.model.entity.ScoredDocument;
import com.document.recommendation.domain.model.to.DocumentTO;
import com.document.recommendation.infra.service.KafkaService;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.common.inject.Inject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
@RestController
public class DocumentController {

    private final @NotNull DocumentService documentService;
    private final @NotNull
    ScoreDocumentsService scoreDocumentService;
    private final @NotNull
    KafkaService kafkaService;

    @RequestMapping(value = "/{document-url}/view" ,method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<HttpStatus> view(@PathVariable(value = "document-url") final String document, @RequestBody final String user) {
        kafkaService.produce(new DocumentTO(document, user));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{document-url}/similar", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<ScoredDocument> similar(@PathVariable(value = "document-url") final String document) {
        return new ResponseEntity<>(scoreDocumentService.findByUrl(document), HttpStatus.OK);
    }

    @RequestMapping( method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteAll() {
        documentService.deleteAll();
        scoreDocumentService.deleteAll();
    }
}