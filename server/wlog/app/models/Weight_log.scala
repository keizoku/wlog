package models

import scala.slick.driver.PostgresDriver.simple._
import Database.threadLocalSession

import models._
import java.sql.Timestamp

case class Weight_log(userid: String, ts: Timestamp, weight: Double)

object Weight_logs extends Table[Weight_log]("weight_log") {

	def userid = column[String]("userid", O DBType "char(20)")
	def ts = column[Timestamp]("ts", O DBType "timestamp")
	def weight = column[Double]("weight", O DBType "double", O.Nullable)

	def ins = userid returning userid

	def * = userid ~ ts ~ weight <> (Weight_log.apply _, Weight_log.unapply _)
	def pk = primaryKey("pk_a", (userid, ts))

	def all(): List[Weight_log] = connectDB {
		Query(Weight_logs).sortBy(_.ts).list
	}

	def findByUserid(userid: String): List[Weight_log] = connectDB {
		Query(Weight_logs).where(_.userid === userid).sortBy(_.ts)list
	}

	def allId(): List[String] = connectDB {
	(
		for (r <-Query(Weight_logs).sortBy(_.userid)) yield (r.userid)
	).list
	}

	def create(userid: String) = connectDB {
		Weight_logs.ins.insert(userid)
	}

	def delete(id: Long) = connectDB {
		Weight_logs.where(_.userid === userid).delete
	}

	def findById(id: Long): List[Weight_log] = connectDB {
		Query(Weight_logs).where(_.userid === userid).list
	}

	def save(record: Weight_log) = connectDB {
		Weight_logs.where(_.userid === record.userid).map(record => record.userid ~ record.ts ~ record.weight).update((record.userid, record.ts, record.weight))
	}

	def connectDB[Any](f: => Any): Any = {
		Database.forURL("jdbc:postgresql://ec2-54-249-212-18.ap-northeast-1.compute.amazonaws.com:5432/wlog", driver = "org.postgresql.Driver",user="keizoku",password="keizoku") withSession {
			f
		}
	}

}
