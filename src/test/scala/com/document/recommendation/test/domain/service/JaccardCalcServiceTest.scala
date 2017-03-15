package com.document.recommendation.test.domain.service

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import java.util.Arrays

import com.document.recommendation.domain.model.entity.Document
import com.document.recommendation.domain.service.provider.JaccardCalcService
import com.document.recommendation.test.support.ScalaTestSupport

@RunWith(classOf[JUnitRunner])
class JaccardCalcServiceTest extends ScalaTestSupport {

  feature("Calculate similarity between three documents") {

    scenario("Pre-determined documents") {

      Given("three documents")
      val doc1 = new Document("www.doc1.com", Arrays.asList("user2", "user3", "user4", "user2"))
      val doc2 = new Document("www.doc2.com", Arrays.asList("user1", "user5", "user4", "user2"))
      val doc3 = new Document("www.doc3.com", Arrays.asList("user1"))

      When("compared with JaccardCalcService")
      Then("the similarity between each of them should be")
      JaccardCalcService.similarity(doc1.getUsers, doc2.getUsers) should be(0.4)
      JaccardCalcService.similarity(doc2.getUsers, doc3.getUsers) should be(0.25)
      JaccardCalcService.similarity(doc1.getUsers, doc3.getUsers) should be(0.0)
    }

  }

}
