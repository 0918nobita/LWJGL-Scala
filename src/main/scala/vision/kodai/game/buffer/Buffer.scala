package vision.kodai.game.buffer

import org.lwjgl.opengl.GL15.glGenBuffers

/** Represents an OpenGL's buffer resource */
class Buffer private (val id: Int)

object Buffer {

  /** Indirectly calls glGenBuffers and returns new Buffer */
  def apply(): Buffer = new Buffer(glGenBuffers())
}
