package test

import org.scalatestplus.play.PlaySpec
import play.api.test.Helpers._
import org.scalatestplus.play.guice.GuiceOneServerPerTest
import play.api.libs.ws.WSClient

class ApplicationSpec extends PlaySpec with GuiceOneServerPerTest {

  "Application" should {
    "test server logic" in {
      val ws = app.injector.instanceOf[WSClient]
      val address = s"http://localhost:$port"
      val response = await(ws.url(address).get())
      assert(response.status === OK)
    }
  }
}
