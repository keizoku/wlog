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

  def details(id: Long) = Action { 

    val records = Records.findById(id) 
	Ok(Json.toJson(records.apply(0)))
  }

	def save(id: Long) = Action(parse.json) { request =>

		val recordJson = request.body
		val record = Json.fromJson[Record](recordJson).get

		try {
			Records.save(record)
			Ok("Saved")
		} catch {
			case e:IllegalArgumentException =>
			BadRequest("Record Not Found")
		}
	}

	implicit val recordWrites = (
  		(__ \ "id").write[Long] and
  		(__ \ "value").write[String]
	)(unlift(Record.unapply))

	implicit val recordReads = (
		(__ \ "id").read[Long] and
		(__ \ "value").read[String]
	)(Record)  

}
