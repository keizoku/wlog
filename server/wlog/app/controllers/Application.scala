package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.Json

import anorm.NotAssigned

import models._

object Application extends Controller {
  
  def index = Action {
    Ok(views.html.index())
  }

  def graph = Action {

	var weight_data = for (r<- Records.all()) yield (r.weight)
	var data = "data : [" + weight_data.mkString(",") + "]"

	var ts_data = for (r<- Records.all()) yield (""""""" + r.ts.toString.substring(0,10) + """"""")
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
