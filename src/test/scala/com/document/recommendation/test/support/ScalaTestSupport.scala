package com.document.recommendation.test.support

import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader
import org.scalatest._
import org.scalatest.mockito.MockitoSugar

abstract class ScalaTestSupport extends FeatureSpec with GivenWhenThen with Matchers with OptionValues with Inside with Inspectors with BeforeAndAfterAll with MockitoSugar {

  override protected def beforeAll(): Unit = {
    FixtureFactoryLoader.loadTemplates("com.document.recommendation.test.template.loader");
  }
}