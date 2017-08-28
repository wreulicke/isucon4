package utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateUtil {

  val UUUU_MM_DD_HH_MM_SS_SSS: DateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss.SSS")
  val UUUU_MM_DD_HH_MM_SS: DateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss")
  val UUUUMMDDHHMMSS: DateTimeFormatter = DateTimeFormatter.ofPattern("uuuuMMddHHmmss")

  def parse(s: String, dtf: DateTimeFormatter): LocalDateTime = LocalDateTime.parse(s, dtf)

  def format(ldt: LocalDateTime, dtf: DateTimeFormatter): String = ldt.format(dtf)

  def format(ldt: LocalDateTime): String = format(ldt, UUUU_MM_DD_HH_MM_SS)
}
