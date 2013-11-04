package models

import scala.slick.driver.PostgresDriver.simple._
import Database.threadLocalSession
import play.api.Logger
import play.api.libs.json._

import models._

import java.sql.Date

case class UserInfo(userid:String, name:String, sex:String, mailAddress:String, birthday:Date, address:String, height:Double, targetWeight:Double)

object UserInfo {

  implicit val readsUserInfo: Reads[UserInfo] = new Reads[UserInfo] {
    def reads(json: JsValue): JsResult[UserInfo] = {
      for {
        userid <- (json \ "userid").validate[String]
        name <- (json \ "name").validate[String]
        sex <- (json \ "sex").validate[String]
        mailAddress<- (json \ "mailAddress").validate[String]
        birthday <- (json \ "birthday").validate[Date]
        address <- (json \ "address").validate[String]
        height<- (json \ "height").validate[Double]
        targetWeight<- (json \ "targetWeight").validate[Double]
      } yield UserInfo(userid, name, sex, mailAddress, birthday, address, height, targetWeight)
    }
  }

}


object UserInfoTable extends Table[UserInfo]("userinfo"){
  
  def userid = column[String]("userid", O DBType "char(20)", O.PrimaryKey)
  
  def name = column[String]("name", O DBType "char(20)", O.NotNull)

  def sex = column[String]("sex", O DBType "char(1)", O.NotNull)
  
  def mailAddress = column[String]("mail_address", O DBType "char(50)")

  def birthday = column[Date]("birthday", O DBType "date")
  
  def address = column[String]("address", O DBType "char(100)", O.Nullable)
  
  def height = column[Double]("height", O DBType "double")
  
  def targetWeight = column[Double]("target_weight", O DBType "double")

  def numberOfLogin = column[Int]("number_of_login", O DBType "integer")
  
  def * = userid ~ name ~ sex ~ mailAddress ~ birthday ~ address ~ height ~ targetWeight <> (UserInfo.apply _, UserInfo.unapply _)
  
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
   	UserInfoTable.where(_.userid === userinfo.userid).map( userinfo => userinfo.name ~ userinfo.sex ~ userinfo.mailAddress ~ userinfo.birthday ~ userinfo.address ~ userinfo.height ~ userinfo.targetWeight).update((userinfo.name, userinfo.sex, userinfo.mailAddress, userinfo.birthday, userinfo.address, userinfo.height, userinfo.targetWeight))
  }
  	
  def connectDB[Any](f: => Any): Any = {
	Database.forURL("jdbc:postgresql://ec2-54-249-212-18.ap-northeast-1.compute.amazonaws.com:5432/wlog", driver = "org.postgresql.Driver",user="keizoku",password="keizoku") withSession {
		f
	}
  }

}
