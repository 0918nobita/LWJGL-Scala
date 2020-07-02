package vision.kodai.game

import org.lwjgl.glfw.GLFW.glfwInit

class Game {
  def init(): Unit = {
    if (!glfwInit())
      throw new IllegalStateException("Unable to initialize GLFW")
  }
}

object Main {
  def main(args: Array[String]): Unit = {
    System.setProperty("org.lwjgl.util.Debug", "true")
    // TODO: UnsatisfiedLinkError が発生しないように修正する
    // new Game().init()
    println("Hello, world!")
  }
}
