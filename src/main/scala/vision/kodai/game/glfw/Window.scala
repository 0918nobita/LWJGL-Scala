package vision.kodai.game.glfw

import org.lwjgl.glfw.{GLFWKeyCallbackI, GLFWWindowSizeCallbackI}

trait Window {
  val id: Long

  var pos: (Int, Int)

  var shouldClose: Boolean

  var size: (Int, Int)

  def setAspectRatio(number: Int, denom: Int): Unit

  def setKeyCallback(callback: GLFWKeyCallbackI): Unit

  def setWindowSizeCallback(callback: GLFWWindowSizeCallbackI): Unit

  def swapBuffers(): Unit
}
