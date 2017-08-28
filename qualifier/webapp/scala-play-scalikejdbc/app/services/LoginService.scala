package services

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import javax.inject.{Inject, Singleton}

import entities.{LoginLog, Users}
import exceptions.BusinessException
import repositories.{LoggingRepository, LoginRepository}

@Singleton
class LoginService @Inject()(loginRepository: LoginRepository, loggingRepository: LoggingRepository) {

  // TODO まとめる
  val THRESHOLD_IP_BAN = 10
  val THRESHOLD_USER_LOCK = 10

  def attemptLogin(login: String, password: String, ip: String): LoginLog = {
    val user = loginRepository.findUserByLogin(login) match {
      case Some(u) => u
      case _ => throw new BusinessException("Wrong username or password")
    }

    checkIpBanned(ip)
    checkUserLocked(user)

    val hash = calculatePasswordHash(password, user.salt)
    if (user.passwordHash == hash) {
      loggingRepository.create(succeeded = true, login, ip, user)
      loginRepository.findLoginLogByUserId(user.id)
    } else {
      loggingRepository.create(succeeded = false, login, ip, user)
      throw new BusinessException("Wrong username or password")
    }
  }

  def checkIpBanned(ip: String): Unit = {
    val failures = loginRepository.countBannedIp(ip)
    if (failures >= THRESHOLD_IP_BAN) throw new BusinessException("You're banned.")
  }

  def checkUserLocked(user: Users): Unit = {
    val failures = loginRepository.countLockedUser(user.id)
    if (failures >= THRESHOLD_USER_LOCK) throw new BusinessException("This account is locked.")
  }

  def calculatePasswordHash(password: String, salt: String): String = {
    val md = MessageDigest.getInstance("SHA-256")
    md.reset()
    val v = "%s:%s".format(password, salt)
    md.update(v.getBytes(StandardCharsets.UTF_8))
    md.digest
      .map("%02x".format(_))
      .mkString
  }
}
