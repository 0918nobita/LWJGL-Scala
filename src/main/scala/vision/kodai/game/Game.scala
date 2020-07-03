package vision.kodai.game

import java.nio.IntBuffer
import javax.swing.JOptionPane

import org.lwjgl.BufferUtils
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW._
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30._
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil.NULL

import scala.util.Using
import scala.util.control.Exception.ultimately

class Game {
  private var window: Long = NULL
  private var vArrayId = 0
  private var vBufferId = 0

  def run(): Unit =
    ultimately {
      finish()
      glfwTerminate()
    } {
      initWindow()
      render()
      glfwDestroyWindow(window)
    }

  private def initWindow(): Unit = {
    if (!glfwInit())
      throw new IllegalStateException("Unable to initialize GLFW")

    // ウィンドウの Hint (特性) を指定する (必ずウィンドウ生成の前に行う)
    glfwDefaultWindowHints() // デフォルトの Hint の読み込み
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)

    window = glfwCreateWindow(300, 300, "Game", NULL, NULL)
    if (window == NULL) {
      JOptionPane.showMessageDialog(
        null,
        "メインウィンドウの生成に失敗しました",
        "Game",
        JOptionPane.ERROR_MESSAGE
      )
      System.exit(1)
    }

    // ESC キーで終了できるようにする
    glfwSetKeyCallback(window, (w, key, _, action, _) => {
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

    // VBO の中身となる FloatBuffer を生成し、頂点座標を書き込む
    // format: off
    val vertices = Array(
      -0.6f,  0.2f,  0.5f,
       0.6f, -0.4f, -0.5f,
       0.8f,  0.6f,  0.0f
    )
    // format: on
    val verticesBuf = BufferUtils.createFloatBuffer(vertices.length)
    verticesBuf.put(vertices)
    // これまでに書き込んだ要素だけを読めるようにする
    verticesBuf.flip()

    // VAO (Vertex Array Object)
    vArrayId = glGenVertexArrays()
    glBindVertexArray(vArrayId)

    // VBO (Vertex Buffer Object)
    vBufferId = glGenBuffers()
    glBindBuffer(GL_ARRAY_BUFFER, vBufferId)
    glBufferData(GL_ARRAY_BUFFER, verticesBuf, GL_STATIC_DRAW)
    glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)

    // VBO のバインドを解除
    glBindBuffer(GL_ARRAY_BUFFER, 0)
    // VAO のバインドを解除
    glBindVertexArray(0)
  }

  private def draw(): Unit = {
    // initGL でセットアップした VAO を再びバインドする
    glBindVertexArray(vArrayId)
    // 最初の頂点属性を使用可能にする
    glEnableVertexAttribArray(0)
    glDrawArrays(GL_TRIANGLES, 0, 3)
    // 描画が完了したので、頂点属性を使用不可にする
    glDisableVertexAttribArray(0)
    glBindVertexArray(0)
  }

  private def finish(): Unit = {
    glDisableVertexAttribArray(0)

    glBindBuffer(GL_ARRAY_BUFFER, 0)
    glDeleteBuffers(vBufferId)

    glBindVertexArray(0)
    glDeleteVertexArrays(vArrayId)

    glfwFreeCallbacks(window)
  }
}
