package davidpeklak.stundenplan.time

object AvailabilityValidation {
  implicit class Validation(availability: Availability) {
    def validate: Availability = {
      DayTimeValidation.Validation(availability.from).validate
      DayTimeValidation.Validation(availability.to).validate
      require(DayTimeValidation.Validation(availability.from) isBefore availability.to, "in Availabilty, 'from' must be before 'to'")
      availability
    }
  }
}
