package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json

import anorm.NotAssigned
import models._

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
	Redirect(routes.Application.displayGraph(userid))
  }

  def displayGraph(userid: String) = Action {

	var weight_data = for (r<- Weight_logs.findByUserid(userid)) yield (r.weight)
	var data = "data : [" + weight_data.mkString(",") + "]"

	var ts_data = for (r<- Weight_logs.findByUserid(userid)) yield (""""""" + r.ts.toString.substring(0,10) + """"""")
	var labels = "[" + ts_data.mkString(",") + "]"
	
	println("data:" + data)
	println("labels:" + labels)

	val datalist: String = data
    Ok(views.html.records.graph(datalist,labels))
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
