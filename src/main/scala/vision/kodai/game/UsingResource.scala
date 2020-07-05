package vision.kodai.game

import cats.Eval
import cats.data.ContT

import scala.util.Using.Releasable
import scala.util.control.Exception.ultimately

object UsingResource {
  def apply[A](
      res: => A
  )(implicit releasable: Releasable[A]): ContT[Eval, Unit, A] = {
    ContT[Eval, Unit, A] { k =>
      lazy val target = res
      ultimately { releasable.release(target) } { k(target) }
    }
  }
}
