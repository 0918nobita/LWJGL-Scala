package vision.kodai.game

import java.nio.FloatBuffer

import org.lwjgl.opengl.GL15.{
  glBindBuffer,
  glBufferData,
  glDeleteBuffers,
  glGenBuffers
}

import scala.util.Using.Releasable

package object buffer {

  /** Represents an OpenGL's buffer resource */
  class Buffer private (val id: Int)

  object Buffer {
    /** Indirectly calls glGenBuffers and returns new Buffer */
    def apply(): Buffer = new Buffer(glGenBuffers())
  }

  implicit object BufferIsReleasable extends Releasable[Buffer] {
    override def release(resource: Buffer): Unit =
      glDeleteBuffers(resource.id)
  }


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

  implicit object BufferBindingIsReleasable extends Releasable[BufferBinding] {
    override def release(resource: BufferBinding): Unit = {
      glBindBuffer(resource.target, 0)
    }
  }
}
