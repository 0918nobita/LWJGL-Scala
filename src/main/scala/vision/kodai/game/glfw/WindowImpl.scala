package vision.kodai.game.glfw

import java.nio.IntBuffer

import org.lwjgl.glfw.GLFW.{
  GLFW_RESIZABLE,
  GLFW_VISIBLE,
  glfwCreateWindow,
  glfwDefaultWindowHints,
  glfwGetWindowPos,
  glfwGetWindowSize,
  glfwSetKeyCallback,
  glfwSetWindowAspectRatio,
  glfwSetWindowPos,
  glfwSetWindowShouldClose,
  glfwSetWindowSize,
  glfwSetWindowSizeCallback,
  glfwSwapBuffers,
  glfwWindowHint,
  glfwWindowShouldClose
}
import org.lwjgl.glfw.{GLFWKeyCallbackI, GLFWWindowSizeCallbackI}
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil.NULL

import scala.util.Using

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
      Right(new WindowImpl(windowId))
    } else {
      Left("ウィンドウの生成に失敗しました")
    }
  }
}

object WindowBuilder {
  def apply(): WindowBuilder[WindowProperty.Empty] = new WindowBuilder
}

private class WindowImpl(val id: Long) extends Window {
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

  def swapBuffers(): Unit = glfwSwapBuffers(id)
}
