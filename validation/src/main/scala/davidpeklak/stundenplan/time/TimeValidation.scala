package davidpeklak.stundenplan.time

object MinuteValidation {
  implicit class Validation(minute: Minute) {
    def validate: Minute = {
      require(minute.value >= 0 && minute.value <= 59, "value of Minute must be between 0 and 59")
      minute
    }
  }
}

object HourValidation {
  implicit class Validation(hour: Hour) {
    def validate: Hour = {
      require(hour.value >= 0 && hour.value <= 23, "value of Hour must be between 0 and 23")
      hour
    }
  }
}

object DayTimeValidation {
  implicit class Validation(dayTime: DayTime) {
    def validate: DayTime = {
      new HourValidation.Validation(dayTime.hour).validate
      new MinuteValidation.Validation(dayTime.minute).validate
      dayTime
    }

    import scala.math.Ordering.Implicits.infixOrderingOps

    def isAfter(other: DayTime): Boolean = {
      (dayTime.hour.value, dayTime.minute.value) > (other.hour.value, other.minute.value)
    }

    def isBefore(other: DayTime): Boolean = {
      (dayTime.hour.value, dayTime.minute.value) < (other.hour.value, other.minute.value)
    }
  }
}
