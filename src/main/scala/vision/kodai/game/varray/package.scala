package vision.kodai.game

import org.lwjgl.opengl.GL20.{
  glDisableVertexAttribArray,
  glEnableVertexAttribArray,
  glVertexAttribPointer
}
import org.lwjgl.opengl.GL30.{
  glBindVertexArray,
  glDeleteVertexArrays,
  glGenVertexArrays
}

import scala.util.Using.Releasable

package object varray {

  /** Represents an OpenGL's vertex array resource */
  class VArray private (val id: Int)

  object VArray {

    /** Indirectly calls glGenVertexArrays and returns new VertexArray */
    def apply(): VArray = new VArray(glGenVertexArrays())
  }

  implicit object VArrayIsReleasable extends Releasable[VArray] {
    override def release(resource: VArray): Unit =
      glDeleteVertexArrays(resource.id)
  }

  class VArrayBinding private (val varray: VArray) {
    def setAttribPointer(
        index: Int,
        size: Int,
        glType: Int,
        normalized: Boolean,
        stride: Int,
        pointer: Long
    ): Unit =
      glVertexAttribPointer(index, size, glType, normalized, stride, pointer)

    def enable(index: Int): Unit = glEnableVertexAttribArray(index)

    def disable(index: Int): Unit = glDisableVertexAttribArray(index)
  }

  object VArrayBinding {

    /** Indirectly calls glBindVertexArray and returns new VertexArrayBinding */
    def apply(varray: VArray): VArrayBinding = {
      glBindVertexArray(varray.id)
      new VArrayBinding(varray)
    }
  }

  implicit object VArrayBindingIsReleasable extends Releasable[VArrayBinding] {
    override def release(resource: VArrayBinding): Unit =
      glBindVertexArray(0)
  }
}
