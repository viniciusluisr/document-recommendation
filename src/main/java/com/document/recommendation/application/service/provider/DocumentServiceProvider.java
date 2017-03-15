package com.document.recommendation.application.service.provider;

import com.document.recommendation.application.service.DocumentService;
import com.document.recommendation.domain.event.UnscoredDocumentsEvent;
import com.document.recommendation.domain.model.entity.Document;
import com.document.recommendation.domain.model.to.DocumentTO;
import com.document.recommendation.domain.repository.mongodb.DocumentRepository;
import com.document.recommendation.domain.util.func.Opt;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.Singleton;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Service
public class DocumentServiceProvider implements DocumentService {

    private final @NotNull DocumentRepository repository;
    private final @NotNull ApplicationEventPublisher publisher;

    @Override
    public void viewDocument(final DocumentTO document) {
        final String url = document.getUrl();
        final String user = document.getUser();

        Opt.ofNullable(repository.findByUrl(url))
            .ifPresent()
                .apply(doc -> {
                    final List<String> users = new ArrayList<>(doc.getUsers());
                    users.add(user);
                    repository.save(new Document(url, users));
                })
            .elseApply(() -> repository.save(new Document(url, Lists.newArrayList(user))));

        UnscoredDocumentsEvent event = new UnscoredDocumentsEvent(repository.findAll());
        publisher.publishEvent(event);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

}
