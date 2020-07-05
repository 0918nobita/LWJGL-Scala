package vision.kodai.game.buffer

import java.nio.FloatBuffer

import org.lwjgl.opengl.GL15.{glBindBuffer, glBufferData}

class BufferBinding private (val target: Int, val buffer: Buffer) {
  def writeData(vertices: FloatBuffer, usage: Int): Unit =
    glBufferData(target, vertices, usage)
}

object BufferBinding {

  /** Indirectly calls glBindBuffer and returns new BufferBinding */
  def apply(target: Int, buffer: Buffer): BufferBinding = {
    glBindBuffer(target, buffer.id)
    new BufferBinding(target, buffer)
  }
}
