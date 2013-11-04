package controllers

import play.api.mvc.{Action, Controller}
import models.UserInfo
import models.UserInfoTable
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.libs.json.Writes._
import play.api.libs.json.Reads._
import play.api.Logger

import java.sql.Date

object UserInfoController extends Controller{

   implicit val recordWrites = (
  		(__ \ "userid").write[String] and
  		(__ \ "name").write[String] and
  		(__ \ "sex").write[String] and
  		(__ \ "mailAddress").write[String] and
  		(__ \ "birthday").write[Date] and
  		(__ \ "address").write[String] and
  		(__ \ "height").write[Double] and
  		(__ \ "targetWeight").write[Double]
   )(unlift(UserInfo.unapply))

  def list = Action{ implicit request =>
  	val userInfoList = UserInfoTable.allUserInfo()
  	Ok(Json.toJson(userInfoList))
  }
  
  
  def details(userid: String) = Action{
    Logger.debug("request userid value is " + userid)
    val userInfo = UserInfoTable.findByUserId(userid)
    Ok(Json.toJson(userInfo.apply(0)))
  }

  def save(id: String) = Action(parse.json) { request =>

	val recordJson = request.body
	Logger.debug("request data is " + recordJson)
	val record = Json.fromJson[UserInfo](recordJson).get

	try {
		UserInfoTable.save(record)
		Ok("Saved")
	} catch {
		case e:IllegalArgumentException =>
		BadRequest("Record Not Found")
	}
  }
  
}
