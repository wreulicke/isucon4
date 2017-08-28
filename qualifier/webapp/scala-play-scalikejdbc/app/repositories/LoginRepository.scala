package repositories

import javax.inject.Singleton

import entities.{LoginLog, Users}
import scalikejdbc._

@Singleton
class LoginRepository {

  def findUserByLogin(login: String)(implicit session: DBSession = AutoSession): Option[Users] = {
    val u = Users.syntax("u")
    withSQL {
      select.from(Users as u).where.eq(u.login, login)
    }.map(Users(u.resultName)).single.apply()
  }

  def countBannedIp(ip: String)(implicit session: DBSession = AutoSession): Long = {
    sql"""
         SELECT COUNT(1) AS failures
           FROM `login_log`
          WHERE `ip` = $ip
            AND `id` > IFNULL(
                       (SELECT `id`
                          FROM `login_log`
                         WHERE `ip` = $ip
                           AND `succeeded` = 1
                         ORDER BY `id` DESC LIMIT 1)
                     , 0)
      """
      .map(col => col.long("failures"))
      .single.apply()
      .getOrElse(0L)
  }

  def countLockedUser(userId: Long)(implicit session: DBSession = AutoSession): Long = {
    sql"""
         SELECT COUNT(1) AS failures
           FROM `login_log`
          WHERE `user_id` = $userId
            AND `id` > IFNULL(
                       (SELECT `id`
                          FROM `login_log`
                         WHERE `user_id` = $userId
                           AND `succeeded` = 1
                         ORDER BY `id` DESC LIMIT 1)
                     , 0)
      """
      .map(col => col.long("failures"))
      .single.apply()
      .getOrElse(0L)
  }

  def findLoginLogByUserId(userId: Long)(implicit session: DBSession = AutoSession): LoginLog = {
    val l = LoginLog.syntax("l")
    val result = withSQL {
      select.from(LoginLog as l).where.eq(l.succeeded, 1).and.eq(l.userId, userId)
        .orderBy(l.id).desc
        .limit(2)
    }.map(LoginLog(l.resultName)).list.apply()

    result.last
  }
}
