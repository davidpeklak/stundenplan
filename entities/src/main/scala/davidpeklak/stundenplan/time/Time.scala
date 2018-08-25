package davidpeklak.stundenplan.time

case class Minute(value: Int) extends AnyVal

object Minute {
  def apply(value: Int): Minute = {
    require(value >= 0 && value <= 59, "value of Minute must be between 0 and 59")
    new Minute(value)
  }
}

case class Hour(value: Int) extends AnyVal

object Hour {
  def apply(value: Int): Hour = {
    require(value >= 0 && value <= 23, "value of Hour must be between 0 and 23")
    new Hour(value)
  }
}

case class DayTime(hour: Hour, minute: Minute)

case class Duration(minutes: Int) extends AnyVal

sealed trait WeekDay

case object Monday extends WeekDay

case object Tuesday extends WeekDay

case object Wednesday extends WeekDay

case object Thursday extends WeekDay

case object Friday extends WeekDay

case object Saturday extends WeekDay

case object Sunday extends WeekDay
