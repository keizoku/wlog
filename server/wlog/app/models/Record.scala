package models

import scala.slick.driver.PostgresDriver.simple._
import Database.threadLocalSession

import models._
import java.sql.Timestamp

case class Record(id: Long, value: String, userid: String, ts: Timestamp, weight: Double)

object Records extends Table[Record]("record") {

	def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
	def value = column[String]("value", O DBType "char(32)", O.Nullable)
	def userid = column[String]("userid", O DBType "varchar(40)", O.Nullable)
	def ts = column[Timestamp]("ts", O DBType "timestamp", O.Nullable)
	def weight = column[Double]("weight", O DBType "double", O.Nullable)

	def * = id ~ value ~ userid ~ ts ~ weight <> (Record, Record.unapply _)
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
		Records.where(_.id === record.id).map(record => record.value ~ record.userid ~ record.ts ~ record.weight).update((record.value, record.userid, record.ts, record.weight))
	}

	def connectDB[Any](f: => Any): Any = {
		Database.forURL("jdbc:postgresql://ec2-54-249-212-18.ap-northeast-1.compute.amazonaws.com:5432/wlog", driver = "org.postgresql.Driver",user="postgres",password="postgres") withSession {
			f
		}
	}

}
