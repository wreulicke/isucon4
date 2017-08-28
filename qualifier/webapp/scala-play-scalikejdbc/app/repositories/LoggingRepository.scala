package repositories

import java.time.LocalDateTime
import javax.inject.Singleton

import entities.{LoginLog, Users}
import scalikejdbc._

@Singleton
class LoggingRepository {

  def create(succeeded: Boolean, login: String, ip: String, user: Users)(implicit session: DBSession = AutoSession): Int = {
    val userId = if (user != null) {
      Some(user.id)
    } else {
      None
    }

    val l = LoginLog.column
    applyUpdate {
      insert.into(LoginLog).namedValues(
        l.createdAt -> LocalDateTime.now(),
        l.userId -> userId,
        l.login -> login,
        l.ip -> ip,
        l.succeeded -> succeeded
      )
    }
  }
}
