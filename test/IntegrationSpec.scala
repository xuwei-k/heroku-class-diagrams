package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

class IntegrationSpec extends Specification {

  "Application" should {

    "work from within a browser" in new WithBrowser(){

      browser.goTo("/")

      browser.pageSource must contain("scala.Predef")

    }

  }

}
