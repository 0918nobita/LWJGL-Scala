package vision.kodai.game.glfw

import org.lwjgl.glfw.GLFW.{
  glfwGetPrimaryMonitor,
  glfwGetVideoMode,
  glfwInit,
  glfwMakeContextCurrent,
  glfwPollEvents,
  glfwShowWindow,
  glfwSwapInterval
}
import org.lwjgl.glfw.GLFWVidMode

class Runtime private () {

  /** 指定した window を OpenGL の描画対象にする */
  def makeContextCurrent(window: Window): Unit =
    glfwMakeContextCurrent(window.id)

  /** Poll for window events. */
  def pollEvents(): Unit = glfwPollEvents()

  def primaryMonitor: Long = glfwGetPrimaryMonitor()

  /** ダブルバッファリングでの、バッファ入れ替えのタイミング (V-sync) を指定する */
  def setSwapInterval(interval: Int): Unit = glfwSwapInterval(interval)

  def showWindow(window: Window): Unit = glfwShowWindow(window.id)

  def videoMode(monitor: Long): GLFWVidMode = glfwGetVideoMode(monitor)
}

object Runtime {
  def apply(): Runtime = {
    if (!glfwInit())
      throw new IllegalStateException("Unable to initialize GLFW")
    new Runtime()
  }
}
