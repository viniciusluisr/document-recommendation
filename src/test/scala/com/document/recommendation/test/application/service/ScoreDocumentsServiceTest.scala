package com.document.recommendation.test.application.service

import java.util

import br.com.six2six.fixturefactory.Fixture
import com.document.recommendation.application.service.ScoreDocumentsService
import com.document.recommendation.application.service.provider.ScoreDocumentsServiceProvider
import com.document.recommendation.domain.event.UnscoredDocumentsEvent
import com.document.recommendation.domain.model.entity.{Document, ScoredDocument}
import com.document.recommendation.domain.repository.elasticsearch.ScoredDocumentRepository
import com.document.recommendation.test.support.ScalaTestSupport
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.index.query.QueryBuilders.matchAllQuery
import org.junit.runner.RunWith
import org.mockito.{Matchers, Mockito}
import org.scalatest.junit.JUnitRunner
import org.springframework.data.domain.{Page, PageImpl, PageRequest}
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate
import org.springframework.data.elasticsearch.core.query.{NativeSearchQuery, NativeSearchQueryBuilder, SearchQuery}

@RunWith(classOf[JUnitRunner])
class ScoreDocumentsServiceTest extends ScalaTestSupport {

  private val searchQuery: NativeSearchQuery = mock[NativeSearchQuery]
  private val nativeSearchQueryBuilder: NativeSearchQueryBuilder = mock[NativeSearchQueryBuilder]
  private val repository: ScoredDocumentRepository = mock[ScoredDocumentRepository]
  private val elasticsearchTemplate: ElasticsearchTemplate = mock[ElasticsearchTemplate]
  private val service: ScoreDocumentsService = new ScoreDocumentsServiceProvider(repository, elasticsearchTemplate)

  feature("Score documents") {

    scenario("Score existing documents") {

      Given("unscored documents")
      val docs: util.List[Document] = Fixture.from(classOf[Document]).gimme(5, "valid")
      val event: UnscoredDocumentsEvent = new UnscoredDocumentsEvent(docs)

      Then("should score documents")
      service score event
      Mockito.verify(repository, Mockito.times(docs.size())).save(Matchers.any(classOf[ScoredDocument]))

    }

  }

}