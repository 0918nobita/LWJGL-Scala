package vision.kodai.game

import javax.swing.JOptionPane

import org.lwjgl.BufferUtils
import org.lwjgl.glfw.GLFW._
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL15._
import org.lwjgl.system.MemoryUtil.NULL

import cats.Eval
import cats.data.ContT
import scala.util.Using
import vision.kodai.game.buffer._
import vision.kodai.game.buffer.Implicits._
import vision.kodai.game.glfw._
import vision.kodai.game.glfw.Implicits._
import vision.kodai.game.varray._
import vision.kodai.game.varray.Implicits._

class Game {
  def run(): Unit = {
    Using(Runtime()) { glfw =>
      val window = genWindow()
      initWindow(glfw, window)

      glfw.makeContextCurrent(window)
      glfw.setSwapInterval(1)
      glfw.showWindow(window)

      // This line is critical for LWJGL's interoperation with GLFW's
      // OpenGL context, or any context that is managed externally.
      // LWJGL detects the context that is current in the current thread,
      // creates the GLCapabilities instance and makes the OpenGL
      // bindings available for use.
      GL.createCapabilities()

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

      val computation: ContT[Eval, Unit, Unit] = for {
        varray <- UsingResource(VArray())
        buffer <- UsingResource(Buffer())
        // Prepare VAO
        _ <- for {
          vaBinding  <- UsingResource(VArrayBinding(varray))
          bufBinding <- UsingResource(BufferBinding(GL_ARRAY_BUFFER, buffer))
        } yield {
          bufBinding.writeData(verticesBuf, GL_STATIC_DRAW)
          vaBinding.setAttribPointer(
            index = 0,
            size = 3,
            GL_FLOAT,
            normalized = false,
            stride = 0,
            pointer = 0
          )
        }
      } yield {
        while (!glfwWindowShouldClose(window.id)) {
          glClear(
            GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT
          ) // clear the flamebuffer

          // initGL でセットアップした VAO を再びバインドする
          Using(VArrayBinding(varray)) { vaBinding =>
            // 最初の頂点属性を使用可能にする
            vaBinding.enable(0)
            glDrawArrays(GL_TRIANGLES, 0, 3)
            // 描画が完了したので、頂点属性を使用不可にする
            vaBinding.disable(0)
          }

          glfwSwapBuffers(window.id)

          // Poll for window events.
          // The key callback above will only be invoked during this call.
          glfwPollEvents()
        }
      }

      computation.run { _ => Eval.now(()) }.value
    }
  }

  private def genWindow(): Window = {
    val builder = new WindowBuilder()
    builder.loadDefaultHints()
    builder.setHint(GLFW_VISIBLE, GLFW_FALSE)
    builder.setHint(GLFW_RESIZABLE, GLFW_TRUE)

    val result = builder.getResult(300, 300, "Game", NULL, NULL)
    result match {
      case Left(msg) =>
        JOptionPane.showMessageDialog(
          null,
          msg,
          "Game",
          JOptionPane.ERROR_MESSAGE
        )
        throw new RuntimeException(msg)
      case Right(w) => w
    }
  }

  private def initWindow(glfw: Runtime, window: Window): Unit = {
    // ESC キーで終了できるようにする
    window.setKeyCallback { (w, key, _, action, _) =>
      if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
        glfwSetWindowShouldClose(w, true)
    }

    window.setAspectRatio(1, 1)

    // ウィンドウをモニターの中央に配置する
    val (width, height) = window.getSize
    val vidMode         = glfw.videoMode(glfw.primaryMonitor)
    window.setPos(
      (vidMode.width() - width) / 2,
      (vidMode.height() - height) / 2
    )

    window.setWindowSizeCallback { (_, width, height) =>
      glViewport(0, 0, width, height)
    }
  }
}
