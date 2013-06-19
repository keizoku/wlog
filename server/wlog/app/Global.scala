import play.api._
import models._
import scala.slick.driver.PostgresDriver.simple._
import Database.threadLocalSession

import java.sql.{Date,Time,Timestamp}

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    Logger.info("Application has started")

    Database.forURL("jdbc:postgresql:wlog", driver = "org.postgresql.Driver",user="postgres",password="postgres") withSession {

      //Records.ddl.create

    }

  }

  override def onStop(app: Application) {
    Logger.info("Application shutdown...")
  }

}
