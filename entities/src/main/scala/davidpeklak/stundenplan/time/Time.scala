package davidpeklak.stundenplan.time

case class Minute(value: Int) extends AnyVal

case class Hour(value: Int) extends AnyVal

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
