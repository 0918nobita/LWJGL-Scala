package vision.kodai.game.varray

import org.lwjgl.opengl.GL30.glGenVertexArrays

/** Represents an OpenGL's vertex array resource */
class VArray private (val id: Int)

object VArray {

  /** Indirectly calls glGenVertexArrays and returns new VertexArray */
  def apply(): VArray = new VArray(glGenVertexArrays())
}
