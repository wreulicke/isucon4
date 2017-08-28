package entities

import java.time.LocalDateTime

import scalikejdbc._
import scalikejdbc.jsr310._

case class LoginLog(id: Long,
                    createdAt: LocalDateTime,
                    userId: Long,
                    login: String,
                    ip: String,
                    succeeded: Byte)

object LoginLog extends SQLSyntaxSupport[LoginLog] {
  def apply(rn: ResultName[LoginLog])(wrs: WrappedResultSet): LoginLog = LoginLog(
    wrs.get(rn.id),
    wrs.get(rn.createdAt),
    wrs.get(rn.userId),
    wrs.get(rn.login),
    wrs.get(rn.ip),
    wrs.get(rn.succeeded))
}
