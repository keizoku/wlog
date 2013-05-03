package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {
  
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def reverse(value: String) = Action {
      import play.api.libs.json.Json
  
      val success = Map("value1" -> value.reverse)
      val json = Json.toJson(success)
  
      Ok(json).as(JSON)
  }
}
