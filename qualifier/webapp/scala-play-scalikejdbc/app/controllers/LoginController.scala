package controllers

import javax.inject._

import exceptions.BusinessException
import forms._
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import services.LoginService
import utils.DateUtil

@Singleton
class LoginController @Inject()(val messagesApi: MessagesApi)(loginService: LoginService)
  extends Controller with I18nSupport {

  val loginForm = Form(
    mapping(
      "login" -> text,
      "password" -> text
    )(LoginForm.apply)(LoginForm.unapply)
  )

  def index = Action { implicit request =>
    val msg = request.flash.get("msg").getOrElse("")
    Ok(views.html.index(loginForm)(msg))
  }

  def login = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => {
        val msg = ""
        BadRequest(views.html.index(formWithErrors)(msg))
      },
      formSuccess => {
        val remoteAddr = request.remoteAddress
        val xForwardedFor = request.headers.get("X-Forwarded-For").getOrElse("")
        val ip = if (xForwardedFor != null && !xForwardedFor.isEmpty) xForwardedFor else remoteAddr
        try {
          val loginLog = loginService.attemptLogin(formSuccess.login, formSuccess.password, ip)
          Redirect(routes.MyPageController.index())
            .withSession(
              "login" -> loginLog.login,
              "ip" -> loginLog.ip,
              "createdAt" -> DateUtil.format(loginLog.createdAt))
        } catch {
          case e: BusinessException => Redirect(routes.LoginController.index()).flashing("msg" -> e.getMessage)
        }
      }
    )
  }
}
