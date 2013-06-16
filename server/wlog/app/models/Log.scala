package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class Log(id: Pk[Long], value: String)

object Log {

  val simple = {
    get[Pk[Long]]("id") ~
    get[String]("value") map {
      case id~value=> Log(id, value)
    }
  }
 
  def findAll(): Seq[Log] = {
    DB.withConnection { implicit connection =>
      SQL("select * from bar").as(Log.simple *)
    }
  }
 
  def insert(log: Log): Unit = {
    DB.withConnection { implicit connection =>
      SQL("insert into log(value) values ({value})").on(
        'value-> log.value
      ).executeUpdate()
    }
  }
}
