package davidpeklak.stundenplan.time

import org.specs2.Specification
import org.specs2.matcher.MatchResult
import org.specs2.specification.core.SpecStructure

class AvailabilityValidationSpec extends Specification{

  def is: SpecStructure = s2"""

In Availability,
  'from' and 'to' must be validated   $validateFromTo
  'from' must be before 'to'          $fromBeforeTo

    """

  import AvailabilityValidation._

  def validateFromTo: MatchResult[_] = {
    Availability(Monday, DayTime(Hour(17), Minute(-5)), DayTime(Hour(18), Minute(50))).validate must throwAn[Exception]
  }

  def fromBeforeTo: MatchResult[_] = {
    val early = DayTime(Hour(13), Minute(5))
    val late = DayTime(Hour(18), Minute(30))

    (Availability(Monday, early, late).validate mustEqual Availability(Monday, early, late)) and
      (Availability(Monday, early, early).validate must throwAn[Exception]) and
      (Availability(Monday, late, early).validate must throwAn[Exception])
  }
}
