package vision.kodai.game.glfw

import org.lwjgl.glfw.GLFW.{
  glfwCreateWindow,
  glfwDefaultWindowHints,
  glfwWindowHint
}
import org.lwjgl.system.MemoryUtil.NULL

sealed trait Property
object Property {
  sealed trait Empty  extends Property
  sealed trait Width  extends Property
  sealed trait Height extends Property
  sealed trait Title  extends Property
  type Mandatory = Empty with Width with Height with Title
}

case class Builder[P <: Property] private (
    width: Int = 0,
    height: Int = 0,
    title: String = ""
) {
  def withWidth(w: Int): Builder[P with Property.Width] =
    this.copy(width = w)

  def withHeight(h: Int): Builder[P with Property.Height] =
    this.copy(height = h)

  def withTitle(t: String): Builder[P with Property.Title] =
    this.copy(title = t)

  def build(implicit ev: P =:= Property.Mandatory) =
    s"Window ($width, $height, `$title`)"
}

object Builder {
  def apply(): Builder[Property.Empty] = new Builder()
}

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
