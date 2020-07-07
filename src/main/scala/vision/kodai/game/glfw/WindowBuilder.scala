package vision.kodai.game.glfw

import org.lwjgl.glfw.GLFW.{
  GLFW_RESIZABLE,
  GLFW_VISIBLE,
  glfwCreateWindow,
  glfwDefaultWindowHints,
  glfwWindowHint
}
import org.lwjgl.system.MemoryUtil.NULL

sealed trait WindowProperty
object WindowProperty {
  sealed trait Empty  extends WindowProperty
  sealed trait Width  extends WindowProperty
  sealed trait Height extends WindowProperty
  sealed trait Title  extends WindowProperty
  type Mandatory = Empty with Width with Height with Title
}

case class WindowBuilder[P <: WindowProperty] private (
    width: Int = 0,
    height: Int = 0,
    title: String = "",
    visible: Option[Boolean] = None,
    resizable: Option[Boolean] = None
) {
  def withWidth(w: Int): WindowBuilder[P with WindowProperty.Width] =
    this.copy(width = w)

  def withHeight(h: Int): WindowBuilder[P with WindowProperty.Height] =
    this.copy(height = h)

  def withTitle(t: String): WindowBuilder[P with WindowProperty.Title] =
    this.copy(title = t)

  def withVisible(flag: Boolean): WindowBuilder[P] =
    this.copy(visible = Some(flag))

  def withResizable(flag: Boolean): WindowBuilder[P] =
    this.copy(resizable = Some(flag))

  /** Indirectly calls `glfwDefaultHints`, `glfwWindowHint` and `glfwCreateWindow` */
  def build(implicit
      ev: P =:= WindowProperty.Mandatory
  ): Either[String, Window] = {
    glfwDefaultWindowHints()

    visible.foreach { flag =>
      glfwWindowHint(GLFW_VISIBLE, GLFWTypes.bool(flag))
    }

    resizable.foreach { flag =>
      glfwWindowHint(GLFW_RESIZABLE, GLFWTypes.bool(flag))
    }

    val windowId = glfwCreateWindow(width, height, title, NULL, NULL)
    if (windowId != NULL) {
      Right(new Window(windowId))
    } else {
      Left("ウィンドウの生成に失敗しました")
    }
  }
}

object WindowBuilder {
  def apply(): WindowBuilder[WindowProperty.Empty] = new WindowBuilder
}
