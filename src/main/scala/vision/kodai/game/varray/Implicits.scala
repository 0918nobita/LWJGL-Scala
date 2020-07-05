package vision.kodai.game.varray

import org.lwjgl.opengl.GL30.{glBindVertexArray, glDeleteVertexArrays}

import scala.util.Using.Releasable

object Implicits {
  implicit object VArrayIsReleasable extends Releasable[VArray] {
    override def release(resource: VArray): Unit =
      glDeleteVertexArrays(resource.id)
  }

  implicit object VArrayBindingIsReleasable extends Releasable[VArrayBinding] {
    override def release(resource: VArrayBinding): Unit =
      glBindVertexArray(0)
  }
}
