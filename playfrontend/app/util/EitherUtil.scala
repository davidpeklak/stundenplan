package util

object EitherUtil {

  implicit class EitherOps[A, B](either: Either[A, B]) {
    def both[C](implicit ac: A <:< C, bc: B <:< C) : C  = {
      either.fold(ac, bc)
    }
  }
}
