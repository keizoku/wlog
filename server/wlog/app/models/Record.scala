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
	(
		for (r <-Query(Records).sortBy(_.id)) yield (r.id)
	).list
	}

	def create(value: String) = connectDB {
		Records.ins.insert(value)
	}

	def delete(id: Long) = connectDB {
		Records.where(_.id === id).delete
	}

	def findById(id: Long): List[Record] = connectDB {
		Query(Records).where(_.id === id).list
	}

	def save(record: Record) = connectDB {
		Records.where(_.id === record.id).map(_.value).update(record.value)
	}

	def connectDB[Any](f: => Any): Any = {
		Database.forURL("jdbc:postgresql:wlog", driver = "org.postgresql.Driver",user="postgres",password="postgres") withSession {
			f
		}
	}

}
