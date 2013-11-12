package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json

import anorm.NotAssigned
import models._

import java.text.SimpleDateFormat

object Application extends Controller {
  
  def index = Action {
    Ok(views.html.index())
  }

  def user = Action {
    Ok(views.html.userinfo())
  }

  def login = Action { implicit request =>
    Ok(views.html.login())
  }

  def loginAction = Action { implicit request =>

	val loginForm = Form(
	  tuple(
	    "userid" -> text,
	    "password" -> text
	  )
	)

	val (userid,password) = loginForm.bindFromRequest.get

    Ok(views.html.menu(userid))
  }

  def inputWeight(userid: String) = Action { implicit request =>

    Ok(views.html.inputWeight(userid))
  }

  def registWeight = Action { implicit request =>

	val loginForm = Form(
	  tuple(
	    "userid" -> text,
	    "weight" -> text
	  )
	)

	val (userid,weight) = loginForm.bindFromRequest.get

	// DB保存
	Weight_logs.create(userid, weight toDouble)

	Redirect(routes.Application.displayGraph(userid))
  }

  def displayGraph(userid: String) = Action {

	var weight_data = for (r<- Weight_logs.findByUserid(userid)) yield (r.weight)
	var data = "data : [" + weight_data.mkString(",") + "]"

	val width = 10
	val min = weight_data.min-width
	val max = weight_data.max+width
	val step = 5

	//var ts_data = for (r<- Weight_logs.findByUserid(userid)) yield (""""""" + r.ts.toString.substring(0,10) + """"""")
	var ts_data = for (r<- Weight_logs.findByUserid(userid)) yield (""""""" + " " + """"""")
	var labels = "[" + ts_data.mkString(",") + "]"
	
	println("data:" + data)
	println("labels:" + labels)

	val datalist: String = data
    Ok(views.html.records.graph(userid, datalist, labels,((max-min)/step).intValue, min.intValue))
  }

  def editProfile(userid: String) = Action { implicit request =>

	val userinfo = UserInfoTable.findByUserId(userid).head

	val yyyyf = new SimpleDateFormat("yyyy");  
	val mmf = new SimpleDateFormat("MM");  
	val ddf = new SimpleDateFormat("dd");  

	val s_yyyy= yyyyf.format(userinfo.birthday);  
	val s_mm  = mmf.format(userinfo.birthday);  
	val s_dd  = ddf.format(userinfo.birthday);  
  

    Ok(views.html.editProfile(userinfo.userid, userinfo.name, userinfo.sex, userinfo.mailAddress, s_yyyy, s_mm, s_dd, userinfo.address, userinfo.height.toString, userinfo.targetWeight.toString))
  }

  def applyProfile = Action { implicit request =>

	val loginForm = Form(
	  tuple(
	    "userid" -> text,
	    "name" -> text,
	    "sex" -> text,
	    "mail_address" -> text,
	    "birthday_yyyy" -> text,
	    "birthday_mm" -> text,
	    "birthday_dd" -> text,
	    "address" -> text,
	    "height" -> text,
	    "target_weight" -> text
	  )
	)

	val (userid, name, sex, mail_address, birthday_yyyy, birthday_mm, birthday_dd, address, height, target_weight) = loginForm.bindFromRequest.get

	val format = new SimpleDateFormat("yyyy/MM/dd")
    val parsed = format.parse(birthday_yyyy + "/" + birthday_mm + "/" +birthday_dd)
    val date = new java.sql.Date(parsed.getTime())

	val userinfo = new UserInfo(userid, name, sex, mail_address, date, address, height.toDouble, target_weight.toDouble)
	UserInfoTable.save(userinfo)

    Ok(views.html.applyProfile(userid, name, sex, mail_address, birthday_yyyy, birthday_mm, birthday_dd, address, height, target_weight))
  }

  def displayMenu(userid: String) = Action { implicit request =>
    Ok(views.html.menu(userid))
  }

  def reverse(value: String) = Action {

      val success = Map("value1" -> value.reverse)
      val json = Json.toJson(success)

	  // DBにvalue1を保存
	  Log.insert(Log(NotAssigned, value.reverse))
	  Records.create(value.reverse)
  
      Ok(json).as(JSON)
  }
}
