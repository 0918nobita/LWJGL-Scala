package vision.kodai.game

object Main {
  def main(args: Array[String]): Unit = {
    System.setProperty("org.lwjgl.system.allocator", "system")
    System.setProperty("org.lwjgl.util.Debug", "true")
    new Game().run()
  }
}
