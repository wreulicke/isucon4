package controllers

import javax.inject.{Inject, Singleton}

import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import services.ReportService

@Singleton
class ReportController @Inject()(reportService: ReportService) extends Controller {

  def report = Action {
    val report = reportService.getReport
    Ok(Json.toJson(report))
  }
}
