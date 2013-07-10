import play.api._
import models._
import scala.slick.driver.PostgresDriver.simple._
import Database.threadLocalSession

import java.sql.{Date,Time,Timestamp}

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    Logger.info("Application has started")
  }

  override def onStop(app: Application) {
    Logger.info("Application shutdown...")
  }

}
