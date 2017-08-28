package services

import javax.inject.{Inject, Singleton}

import repositories.ReportRepository

@Singleton
class ReportService @Inject()(reportRepository: ReportRepository) {

  // TODO まとめる
  val THRESHOLD_IP_BAN = 10
  val THRESHOLD_USER_LOCK = 10

  def getReport: Map[String, List[String]] = {
    // IP
    val ips = reportRepository.getBannedIpsNotSucceed(THRESHOLD_IP_BAN)
    val bannedIpsLastSucceeds = reportRepository.getBannedIpsLastSucceed
    val filteredIps = bannedIpsLastSucceeds
      .map(t => reportRepository.getBannedIpsLastSucceedCounts(t._1, t._2))
      .map {
        case Some(t) => t
        case _ => throw new RuntimeException()
      }
      .filter(_._2 >= THRESHOLD_IP_BAN)
      .map(_._1)
    val ipList = ips ::: filteredIps

    // User
    val loginLogs = reportRepository.getLockedUsersNotSucceed(THRESHOLD_USER_LOCK)
    val lockedUsersLastSucceeds = reportRepository.getLockedUsersLastSucceed
    val filteredUsers = lockedUsersLastSucceeds
      .map(t => reportRepository.getLockedUsersLastSucceedCounts(t._1, t._2, t._3))
      .map {
        case Some(t) => t
        case _ => throw new RuntimeException()
      }
      .filter(_._2 >= THRESHOLD_USER_LOCK)
      .map(_._1)
    val userList = loginLogs ::: filteredUsers

    Map(
      "banned_ips" -> ipList,
      "locked_users" -> userList
    )
  }
}
