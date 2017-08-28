package controllers

import javax.inject._

import com.google.common.base.Strings
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._

@Singleton
class MyPageController @Inject()(val messagesApi: MessagesApi) extends Controller with I18nSupport {

  def index = Action { implicit request =>
    val login = request.session.get("login").getOrElse("")
    val ip = request.session.get("ip").getOrElse("")
    val createdAt = request.session.get("createdAt").getOrElse("")

    if (Strings.isNullOrEmpty(login) || Strings.isNullOrEmpty(ip) || Strings.isNullOrEmpty(createdAt)) {
      Redirect(routes.LoginController.index()).flashing("msg" -> "You must be logged in")
    } else {
      Ok(views.html.mypage(login, ip, createdAt))
    }
  }
}
