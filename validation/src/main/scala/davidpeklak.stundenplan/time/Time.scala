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
