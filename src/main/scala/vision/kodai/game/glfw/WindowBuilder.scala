package vision.kodai.game.glfw

import org.lwjgl.glfw.GLFW.{
  glfwCreateWindow,
  glfwDefaultWindowHints,
  glfwWindowHint
}
import org.lwjgl.system.MemoryUtil.NULL

class WindowBuilder {

  /** デフォルトの各種 Hint (特性) の読み込み */
  def loadDefaultHints(): Unit = glfwDefaultWindowHints()

  /** ウィンドウの Hint (特性) を指定する */
  def setHint(hint: Int, value: Int): Unit = glfwWindowHint(hint, value)

  /** Indirectly calls glfwCreateWindow */
  def build(
      width: Int,
      height: Int,
      title: String,
      monitor: Long,
      share: Long
  ): Either[String, Window] = {
    val windowId = glfwCreateWindow(width, height, title, monitor, share)
    if (windowId != NULL) {
      Right(new Window(windowId))
    } else {
      Left("ウィンドウの生成に失敗しました")
    }
  }
}
