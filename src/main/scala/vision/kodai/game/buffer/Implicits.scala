package vision.kodai.game.buffer

import org.lwjgl.opengl.GL15.{glBindBuffer, glDeleteBuffers}

import scala.util.Using.Releasable

object Implicits {
  implicit object BufferIsReleasable extends Releasable[Buffer] {
    override def release(resource: Buffer): Unit =
      glDeleteBuffers(resource.id)
  }

  implicit object BufferBindingIsReleasable extends Releasable[BufferBinding] {
    override def release(resource: BufferBinding): Unit =
      glBindBuffer(resource.target, 0)
  }
}
