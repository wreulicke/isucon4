package repositories

import javax.inject.Singleton

import scalikejdbc._

@Singleton
class ReportRepository {

  def getBannedIpsNotSucceed(threshold: Int)(implicit session: DBSession = AutoSession): List[String] = {
    sql"""
      SELECT `ip`
        FROM (SELECT `ip`
                   , MAX(`succeeded`) AS max_succeeded
                   , COUNT(1) AS cnt
                FROM `login_log`
               GROUP BY `ip`) AS t0
       WHERE t0.`max_succeeded` = 0
         AND t0.`cnt` >= $threshold
      """
      .map(col => col.string("ip"))
      .list.apply()
  }

  def getBannedIpsLastSucceed()(implicit session: DBSession = AutoSession): List[(Long, String)] = {
    sql"""
      SELECT `ip`
           , MAX(`id`) AS id
        FROM `login_log`
       WHERE `succeeded` = 1
       GROUP BY `ip`
      """
      .map(col => (col.long("id"), col.string("ip")))
      .list.apply()
  }

  def getBannedIpsLastSucceedCounts(id: Long, ip: String)(implicit session: DBSession = AutoSession): Option[(String, Long)] = {
    sql"""
      SELECT COUNT(1) AS cnt
        FROM `login_log`
       WHERE `ip` = $ip
         AND `id` > $id
      """
      .map(col => (ip, col.long("cnt")))
      .single.apply()
  }

  def getLockedUsersNotSucceed(threshold: Int)(implicit session: DBSession = AutoSession): List[String] = {
    sql"""
      SELECT `login`
        FROM (SELECT `user_id`
                   , `login`
                   , MAX(`succeeded`) AS `max_succeeded`
                   , COUNT(1) AS cnt
                FROM `login_log`
               GROUP BY `user_id`) AS l
       WHERE l.`user_id` IS NOT NULL
         AND l.`max_succeeded` = 0
         AND l.`cnt` >= $threshold
      """
      .map(col => col.string("login"))
      .list.apply()
  }

  def getLockedUsersLastSucceed()(implicit session: DBSession = AutoSession): List[(Long, Long, String)] = {
    sql"""
      SELECT `user_id`
           , `login`
           , MAX(`id`) AS id
        FROM `login_log`
       WHERE `user_id` IS NOT NULL
         AND `succeeded` = 1
       GROUP BY `user_id`
      """
      .map(col => (col.long("id"), col.long("user_id"), col.string("login")))
      .list.apply()
  }

  def getLockedUsersLastSucceedCounts(id: Long, userId: Long, login: String)(implicit session: DBSession = AutoSession): Option[(String, Long)] = {
    sql"""
      SELECT COUNT(1) AS cnt
        FROM `login_log`
       WHERE `user_id` = $userId
         AND `id` > $id
      """
      .map(col => (login, col.long("cnt")))
      .single.apply()
  }
}
