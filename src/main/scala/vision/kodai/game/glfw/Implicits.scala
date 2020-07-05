package vision.kodai.game.glfw

import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.{glfwDestroyWindow, glfwTerminate}

import scala.util.Using.Releasable

object Implicits {
  implicit object GLFWRuntimeIsReleasable extends Releasable[Runtime] {
    override def release(resource: Runtime): Unit = glfwTerminate()
  }

  implicit object WindowIsReleasable extends Releasable[Window] {
    override def release(resource: Window): Unit = {
      glfwFreeCallbacks(resource.id)
      glfwDestroyWindow(resource.id)
    }
  }
}
