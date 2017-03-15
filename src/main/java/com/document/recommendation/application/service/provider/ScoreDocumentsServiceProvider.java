package com.document.recommendation.application.service.provider;

import com.document.recommendation.application.service.ScoreDocumentsService;
import com.document.recommendation.domain.event.UnscoredDocumentsEvent;
import com.document.recommendation.domain.model.entity.Document;
import com.document.recommendation.domain.model.entity.Score;
import com.document.recommendation.domain.model.entity.ScoredDocument;
import com.document.recommendation.domain.repository.elasticsearch.ScoredDocumentRepository;
import com.document.recommendation.domain.service.provider.JaccardCalcService;
import com.document.recommendation.infra.exception.DocumentNotFoundException;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;


@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Service
public class ScoreDocumentsServiceProvider implements ScoreDocumentsService {

    private final @NotNull ScoredDocumentRepository repository;
    private final @NotNull
    ElasticsearchTemplate elasticsearchTemplate;

    @org.springframework.context.event.EventListener()
    @Override
    public void score(final UnscoredDocumentsEvent event) {

            final Map<String, List<Score>> scored = new HashMap<>();
            Iterable<Document> docs = event.getDocs();
            docs.forEach(d1 -> {
                final List<Score> preparing = new ArrayList<>();
                Lists.newArrayList(docs)
                        .stream()
                        .filter(d2 -> d1 != d2)
                        .forEach(d2 ->
                                preparing.add(new Score(d2.getUrl(), JaccardCalcService.similarity(d1.getUsers(), d2.getUsers()))));

                scored.put(d1.getUrl(), preparing);
            });

            save(scored);
    }

    @Override
    public ScoredDocument findByUrl(final String url) {

        final SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery())
                .withFilter(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("url", url)))
                .build();

        final Page<ScoredDocument> result = elasticsearchTemplate.queryForPage(searchQuery,ScoredDocument.class);

        return result.getContent()
                .stream()
                    .findFirst()
                    .orElseThrow(() -> new DocumentNotFoundException("Documento não encontrado com a url: " + url));
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    private void save(final Map<String, List<Score>> docs) {
        docs.forEach((doc, scores) -> {
            final Set<Score> sorted = scores
                .stream()
                    .sorted(Comparator.comparing(Score::getScore).reversed())
                    .collect(Collectors.toCollection(LinkedHashSet::new));

            repository.save(new ScoredDocument(doc, Lists.newArrayList(sorted)));
        });
    }


}
