package vision.kodai.game.glfw

import java.nio.IntBuffer

import org.lwjgl.glfw.GLFW.{
  glfwGetWindowSize,
  glfwSetKeyCallback,
  glfwSetWindowAspectRatio,
  glfwSetWindowPos,
  glfwSetWindowShouldClose,
  glfwSetWindowSizeCallback,
  glfwWindowShouldClose
}
import org.lwjgl.glfw.{GLFWKeyCallbackI, GLFWWindowSizeCallbackI}
import org.lwjgl.system.MemoryStack

import scala.util.Using

class Window(val id: Long) {
  def shouldClose: Boolean = glfwWindowShouldClose(id)

  def shouldClose_=(flag: Boolean): Unit = glfwSetWindowShouldClose(id, flag)

  def getSize: (Int, Int) = {
    Using(MemoryStack.stackPush()) { stack =>
      val pWidth: IntBuffer  = stack.mallocInt(1)
      val pHeight: IntBuffer = stack.mallocInt(1)
      glfwGetWindowSize(id, pWidth, pHeight)
      (pWidth.get(0), pHeight.get(0))
    }.get
  }

  def setAspectRatio(number: Int, denom: Int): Unit =
    glfwSetWindowAspectRatio(id, number, denom)

  def setKeyCallback(callback: GLFWKeyCallbackI): Unit =
    glfwSetKeyCallback(id, callback)

  def setPos(xPos: Int, yPos: Int): Unit = glfwSetWindowPos(id, xPos, yPos)

  def setWindowSizeCallback(callback: GLFWWindowSizeCallbackI): Unit =
    glfwSetWindowSizeCallback(id, callback)
}
