package controllers

import play.api.mvc.{Action, Controller}
import models.Record
import models.Records

import play.api.libs.json._
import play.api.libs.functional.syntax._

object RecordsController extends Controller {

  def list = Action { implicit request =>

    val records = Records.allId() 
	Ok(Json.toJson(records))
  }

  def listAll = Action { implicit request =>

    val records = Records.all() 
	Ok(Json.toJson(records))
  }

  def details(id: Long) = Action { 

    val records = Records.findBy(id) 
	Ok(Json.toJson(records.apply(0)))
  }

	implicit val recordWrites = (
  		(__ \ "id").write[Long] and
  		(__ \ "value").write[String]
	)(unlift(Record.unapply))
}
