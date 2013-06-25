package models

import scala.slick.driver.PostgresDriver.simple._
import Database.threadLocalSession

import models._

case class Record(id: Long, value: String)

object Records extends Table[Record]("record") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def value = column[String]("value", O DBType "char(32)", O.Nullable)
  def * = id ~ value <> (Record, Record.unapply _)
  def ins = value returning id

  def all(): List[Record] = connectDB {
    Query(Records).sortBy(_.id).list
  }

  def allId(): List[Long] = connectDB {
    //Query(Records).sortBy(_.id).list
	(
		for (r <- Records) yield (r.id)
	).list
  }

  def create(value: String) = connectDB {
    Records.ins.insert(value)
  }

  def delete(id: Long) = connectDB {
    Records.where(_.id === id).delete
  }

  def findBy(id: Long): List[Record] = connectDB {
    Query(Records).where(_.id === id).list
  }

  def connectDB[Any](f: => Any): Any = {
    Database.forURL("jdbc:postgresql:wlog", driver = "org.postgresql.Driver",user="postgres",password="postgres") withSession {
      f
    }
  }

}
