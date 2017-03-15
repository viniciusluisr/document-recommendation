package com.document.recommendation.test.application.service

import java.util

import br.com.six2six.fixturefactory.Fixture
import com.document.recommendation.application.service.provider.DocumentServiceProvider
import com.document.recommendation.application.service.{DocumentService, ScoreDocumentsService}
import com.document.recommendation.domain.event.UnscoredDocumentsEvent
import com.document.recommendation.domain.model.entity.Document
import com.document.recommendation.domain.model.to.DocumentTO
import com.document.recommendation.domain.repository.mongodb.DocumentRepository
import com.document.recommendation.infra.exception.InvalidDocumentFormatException
import com.document.recommendation.test.support.ScalaTestSupport
import org.junit.runner.RunWith
import org.mockito.{Matchers, Mockito}
import org.scalatest.junit.JUnitRunner
import org.springframework.context.ApplicationEventPublisher

@RunWith(classOf[JUnitRunner])
class DocumentServiceTest extends ScalaTestSupport {

  private val repository: DocumentRepository = mock[DocumentRepository]
  private val publisher: ApplicationEventPublisher = mock[ApplicationEventPublisher]
  private val service: DocumentService = new DocumentServiceProvider(repository, publisher)


  feature("View documents") {

    scenario("View an existing document") {

      Given("a document url and an user")
      val user = "user1"
      val doc = "www.globo.com/v1234332"

      When("the document exists")
      val expected: Document = existingDocument()
      Mockito.when(repository.findByUrl(doc)).thenReturn(expected)

      Then("should save a new Document with the previews document's users")
      And("should rescore all documents")
      Mockito.when(repository.findAll()).thenReturn(allDocs())

      service.viewDocument(new DocumentTO(doc, user))
      val order = Mockito.inOrder(repository, publisher)

      order verify repository findByUrl doc
      order verify repository save Matchers.any(classOf[Document])
      order verify repository findAll()
      order verify publisher publishEvent Matchers.any(classOf[UnscoredDocumentsEvent])

    }

    scenario("View an inexisting document") {

      Given("a document url and an user")
      val user = "user1"
      val doc = "www.globo.com/v1234332"

      When("the document not exists")
      Then("should create a new document with the given url and user")
      And("should rescore all documents")
      Mockito.when(repository.findAll()).thenReturn(allDocs())

      service.viewDocument(new DocumentTO(doc, user))

      val order = Mockito.inOrder(repository, publisher)
      order verify repository findByUrl doc
      order verify repository save Matchers.any(classOf[Document])
      order verify repository findAll()
      order verify publisher publishEvent Matchers.any(classOf[UnscoredDocumentsEvent])

    }

    scenario("View an null document") {

      Given("an invalid document url and an user")
      val user = "user1"
      val doc = ""

      Then("shouldn't save the document")
      intercept[InvalidDocumentFormatException] {
        service.viewDocument(new DocumentTO(doc, user))
      }

    }

    scenario("View a document with a null user") {

      Given("an a document url and an invalid user")
      val user = ""
      val doc = "www.globo.com/v123433"

      Then("shouldn't save the document")
      intercept[InvalidDocumentFormatException] {
        service.viewDocument(new DocumentTO(doc, user))
      }

    }

  }

  def existingDocument(): Document = Fixture.from(classOf[Document]).gimme("valid")
  def allDocs(): util.List[Document] = Fixture.from(classOf[Document]).gimme(6, "valid")

}