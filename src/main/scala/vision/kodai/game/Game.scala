package vision.kodai.game

import java.nio.IntBuffer

import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.{
  GLFW_FALSE,
  GLFW_KEY_ESCAPE,
  GLFW_RELEASE,
  GLFW_RESIZABLE,
  GLFW_TRUE,
  GLFW_VISIBLE,
  glfwCreateWindow,
  glfwDefaultWindowHints,
  glfwDestroyWindow,
  glfwGetPrimaryMonitor,
  glfwGetVideoMode,
  glfwGetWindowSize,
  glfwInit,
  glfwMakeContextCurrent,
  glfwPollEvents,
  glfwSetKeyCallback,
  glfwSetWindowPos,
  glfwSetWindowAspectRatio,
  glfwSetWindowShouldClose,
  glfwSetWindowSizeCallback,
  glfwShowWindow,
  glfwSwapBuffers,
  glfwSwapInterval,
  glfwTerminate,
  glfwWindowHint,
  glfwWindowShouldClose
}
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.{
  GL_COLOR_BUFFER_BIT,
  GL_DEPTH_BUFFER_BIT,
  GL_TRIANGLES,
  glBegin,
  glClear,
  glClearColor,
  glEnd,
  glVertex3f,
  glViewport
}
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil.NULL

import scala.util.Using
import scala.util.control.Exception.ultimately

class Game {
  private var window: Long = NULL

  def run(): Unit = {
    ultimately {
      glfwFreeCallbacks(window)
      glfwTerminate()
    } {
      initWindow()
      render()
      glfwDestroyWindow(window)
    }
  }

  private def initWindow(): Unit = {
    if (!glfwInit())
      throw new IllegalStateException("Unable to initialize GLFW")

    // ウィンドウの Hint (特性) を指定する (必ずウィンドウ生成の前に行う)
    glfwDefaultWindowHints() // デフォルトの Hint の読み込み
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)

    // ウィンドウ生成 (失敗すると異常終了)
    window = glfwCreateWindow(300, 300, "Game", NULL, NULL)
    if (window == NULL) throw new RuntimeException("ウィンドウの生成に失敗しました")

    // ESC キーで終了できるようにする
    glfwSetKeyCallback(window, (w, key, scancode, action, mods) => {
      if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
        glfwSetWindowShouldClose(w, true)
    })

    glfwSetWindowAspectRatio(window, 1, 1)

    // ウィンドウをモニターの中央に配置する
    Using(MemoryStack.stackPush()) { stack =>
      val pWidth: IntBuffer = stack.mallocInt(1)
      val pHeight: IntBuffer = stack.mallocInt(1)
      glfwGetWindowSize(window, pWidth, pHeight)
      val vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor())
      glfwSetWindowPos(
        window,
        (vidMode.width() - pWidth.get(0)) / 2,
        (vidMode.height() - pHeight.get(0)) / 2
      )
    }

    // window を OpenGL の描画対象にする
    glfwMakeContextCurrent(window)

    // ダブルバッファリングでの、バッファ入れ替えのタイミング (V-sync) を指定する
    glfwSwapInterval(1)

    glfwShowWindow(window)
  }

  private def render(): Unit = {
    // This line is critical for LWJGL's interoperation with GLFW's
    // OpenGL context, or any context that is managed externally.
    // LWJGL detects the context that is current in the current thread,
    // creates the GLCapabilities instance and makes the OpenGL
    // bindings available for use.
    GL.createCapabilities()

    initGL()

    while (!glfwWindowShouldClose(window)) {
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT) // clear the flamebuffer

      draw()

      glfwSwapBuffers(window)

      // Poll for window events.
      // The key callback above will only be invoked during this call.
      glfwPollEvents()
    }
  }

  private def initGL(): Unit = {
    glfwSetWindowSizeCallback(window, (_, width, height) => {
      glViewport(0, 0, width, height)
    })

    glClearColor(0.3f, 0.3f, 0.5f, 0.0f)
  }

  private def draw(): Unit = {
    glBegin(GL_TRIANGLES)
    glVertex3f(-0.6f, 0.2f, 0.5f)
    glVertex3f(0.6f, -0.4f, -0.5f)
    glVertex3f(0.8f, 0.6f, 0.0f)
    glEnd()
  }
}
