package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.Json

import anorm.NotAssigned

import models.Log

object Application extends Controller {
  
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def reverse(value: String) = Action {

      val success = Map("value1" -> value.reverse)
      val json = Json.toJson(success)

	  // DBにvalue1を保存
	  Log.insert(Log(NotAssigned, value.reverse))
  
      Ok(json).as(JSON)
  }
}
