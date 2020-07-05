package vision.kodai.game

import glfw.Builder

object Main {
  def main(args: Array[String]): Unit = {
    val window =
      Builder().withWidth(600).withHeight(400).withTitle("Game").build
    println(window) // => Window (600, 400, `Game`)

    System.setProperty("org.lwjgl.system.allocator", "system")
    System.setProperty("org.lwjgl.util.Debug", "true")
    new Game().run()
  }
}
