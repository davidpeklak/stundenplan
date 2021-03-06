package davidpeklak.stundenplan.time

import org.specs2.Specification
import org.specs2.matcher.MatchResult
import org.specs2.specification.core.SpecStructure

class MinuteValidationSpec extends Specification {
  def is: SpecStructure = s2"""

Minute must be between 0 and 59   $e1

    """

  def e1: MatchResult[_] = {
    import MinuteValidation._

    (Minute(32).validate mustEqual Minute(32)) and
      (Minute(59).validate mustEqual Minute(59)) and
      (Minute(0).validate mustEqual Minute(0)) and
      (Minute(69).validate must throwAn[Exception]) and
      (Minute(-1).validate must throwAn[Exception])
  }
}

class HourValidationSpec extends Specification {
  def is: SpecStructure = s2"""

Hour must be between 0 and 23   $e1

    """

  def e1: MatchResult[_] = {
    import HourValidation._

    (Hour(13).validate mustEqual Hour(13)) and
      (Hour(23).validate mustEqual Hour(23)) and
      (Hour(0).validate mustEqual Hour(0)) and
      (Hour(24).validate must throwAn[Exception]) and
      (Hour(-1).validate must throwAn[Exception])
  }
}

class DayTimeValidationSpec extends Specification {
  def is: SpecStructure = s2"""

DayTime validation
  must have valid hour and minutes   $dayTimeValidate
  test isAfter                       $testIsAfter
  test isBefore                      $testIsBefore

    """

  import DayTimeValidation._

  def dayTimeValidate: MatchResult[_] = {
    (DayTime(Hour(13), Minute(25)).validate mustEqual DayTime(Hour(13), Minute(25))) and
      (DayTime(Hour(24), Minute(25)).validate must throwAn[Exception]) and
      (DayTime(Hour(13), Minute(-1)).validate must throwAn[Exception])
  }

  def testIsAfter: MatchResult[_] = {
    (DayTime(Hour(13), Minute(20)) isAfter DayTime(Hour(12), Minute(50)) must beTrue) and
      (DayTime(Hour(13), Minute(20)) isAfter DayTime(Hour(14), Minute(50)) must beFalse) and
      (DayTime(Hour(13), Minute(20)) isAfter DayTime(Hour(13), Minute(20)) must beFalse)
  }

  def testIsBefore: MatchResult[_] = {
    (DayTime(Hour(10), Minute(20)) isBefore DayTime(Hour(12), Minute(50)) must beTrue) and
      (DayTime(Hour(13), Minute(20)) isBefore DayTime(Hour(10), Minute(50)) must beFalse) and
      (DayTime(Hour(13), Minute(20)) isBefore DayTime(Hour(13), Minute(20)) must beFalse)
  }
}
