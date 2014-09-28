package test

import org.specs2.mutable._
import org.openqa.selenium.chrome.ChromeDriver
import play.api.test._
import play.api.test.Helpers._

class IntegrationSpec extends Specification {

  "Application" should {

    "work from within a browser" in new WithBrowser(new ChromeDriver){

      browser.goTo("/")

      browser.pageSource must contain("scala.Predef")

    }

  }

}
