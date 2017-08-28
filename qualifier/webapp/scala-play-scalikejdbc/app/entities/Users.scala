package entities

import scalikejdbc._

case class Users(id: Long,
                 login: String,
                 passwordHash: String,
                 salt: String)

object Users extends SQLSyntaxSupport[Users] {
  def apply(rn: ResultName[Users])(wrs: WrappedResultSet): Users = Users(
    wrs.get(rn.id),
    wrs.get(rn.login),
    wrs.get(rn.passwordHash),
    wrs.get(rn.salt))
}
