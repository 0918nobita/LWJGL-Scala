package vision.kodai.game.glfw

import org.lwjgl.glfw.GLFW.{
  glfwInit,
  glfwMakeContextCurrent,
  glfwShowWindow,
  glfwSwapInterval
}

class Runtime private () {

  /** 指定した window を OpenGL の描画対象にする */
  def makeContextCurrent(window: Window): Unit =
    glfwMakeContextCurrent(window.id)

  /** ダブルバッファリングでの、バッファ入れ替えのタイミング (V-sync) を指定する */
  def setSwapInterval(interval: Int): Unit = glfwSwapInterval(interval)

  def showWindow(window: Window): Unit = glfwShowWindow(window.id)
}

object Runtime {
  def apply(): Runtime = {
    if (!glfwInit())
      throw new IllegalStateException("Unable to initialize GLFW")
    new Runtime()
  }
}
