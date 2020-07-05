package vision.kodai.game.glfw

import java.nio.IntBuffer

import org.lwjgl.glfw.GLFW.{
  glfwGetWindowPos,
  glfwGetWindowSize,
  glfwSetKeyCallback,
  glfwSetWindowAspectRatio,
  glfwSetWindowPos,
  glfwSetWindowShouldClose,
  glfwSetWindowSize,
  glfwSetWindowSizeCallback,
  glfwWindowShouldClose
}
import org.lwjgl.glfw.{GLFWKeyCallbackI, GLFWWindowSizeCallbackI}
import org.lwjgl.system.MemoryStack

import scala.util.Using

class Window(val id: Long) {
  def pos: (Int, Int) = {
    Using(MemoryStack.stackPush()) { stack =>
      val pXPos: IntBuffer = stack.mallocInt(1)
      val pYPos: IntBuffer = stack.mallocInt(1)
      glfwGetWindowPos(id, pXPos, pYPos)
      (pXPos.get(0), pYPos.get(0))
    }.get
  }

  def pos_=(pos: (Int, Int)): Unit = glfwSetWindowPos(id, pos._1, pos._2)

  def shouldClose: Boolean = glfwWindowShouldClose(id)

  def shouldClose_=(flag: Boolean): Unit = glfwSetWindowShouldClose(id, flag)

  def size: (Int, Int) = {
    Using(MemoryStack.stackPush()) { stack =>
      val pWidth: IntBuffer  = stack.mallocInt(1)
      val pHeight: IntBuffer = stack.mallocInt(1)
      glfwGetWindowSize(id, pWidth, pHeight)
      (pWidth.get(0), pHeight.get(0))
    }.get
  }

  def size_=(s: (Int, Int)): Unit = glfwSetWindowSize(id, s._1, s._2)

  def setAspectRatio(number: Int, denom: Int): Unit =
    glfwSetWindowAspectRatio(id, number, denom)

  def setKeyCallback(callback: GLFWKeyCallbackI): Unit =
    glfwSetKeyCallback(id, callback)

  def setWindowSizeCallback(callback: GLFWWindowSizeCallbackI): Unit =
    glfwSetWindowSizeCallback(id, callback)
}
