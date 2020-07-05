package vision.kodai.game.varray

import org.lwjgl.opengl.GL20.{
  glDisableVertexAttribArray,
  glEnableVertexAttribArray,
  glVertexAttribPointer
}
import org.lwjgl.opengl.GL30.glBindVertexArray

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
