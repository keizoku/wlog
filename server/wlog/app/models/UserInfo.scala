package models

import scala.slick.driver.PostgresDriver.simple._
import Database.threadLocalSession
import play.api.Logger
import play.api.libs.json._

import models._

case class UserInfo(userid:String, name:String, address:String)

object UserInfo {

  implicit val readsUserInfo: Reads[UserInfo] = new Reads[UserInfo] {
    def reads(json: JsValue): JsResult[UserInfo] = {
      for {
        userid <- (json \ "userid").validate[String]
        name <- (json \ "name").validate[String]
        address <- (json \ "address").validate[String]
      } yield UserInfo(userid, name, address)
    }
  }

}


object UserInfoTable extends Table[UserInfo]("userinfo"){
  
  def userid = column[String]("userid", O DBType "char(20)", O.PrimaryKey)
  
  def name = column[String]("name", O DBType "char(20)", O.NotNull)
  
  def mailAddress = column[String]("mail_address", O DBType "char(50)")
  
  def address = column[String]("address", O DBType "char(100)", O.Nullable)
  
  def height = column[Double]("height", O DBType "double")
  
  def targetWeight = column[Double]("target_weight", O DBType "double")

  def numberOfLogin = column[Int]("number_of_login", O DBType "integer")
  
  def * = userid ~ name ~ address <> (UserInfo.apply _, UserInfo.unapply _)
  
  def ins = name returning userid

  	def all(): List[UserInfo] = connectDB {
		Query(UserInfoTable).sortBy(_.userid).list
	}
  
  def allUserInfo(): List[String] = connectDB {
    (
        for( r <-Query(UserInfoTable).sortBy(_.userid)) yield(r.userid)
    ).list
  }
  
  def findByUserId(id: String): List[UserInfo] = connectDB{
		Logger.debug("where id = " + id)
		Query(UserInfoTable).where(_.userid === id).list
  }
  
  def save(userinfo: UserInfo) = connectDB {
//	  UserInfoTable.where(_.userid === userinfo.userid).map(_.name).update(userinfo.name)
   	  UserInfoTable.where(_.userid === userinfo.userid).map( userinfo => userinfo.name ~ userinfo.address).update((userinfo.name, userinfo.address))
  }
  	
  def connectDB[Any](f: => Any): Any = {
	Database.forURL("jdbc:postgresql://ec2-54-249-212-18.ap-northeast-1.compute.amazonaws.com:5432/wlog", driver = "org.postgresql.Driver",user="postgres",password="postgres") withSession {
		f
	}
  }

}