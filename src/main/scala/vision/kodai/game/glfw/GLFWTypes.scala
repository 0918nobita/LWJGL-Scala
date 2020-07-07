package vision.kodai.game.glfw

import org.lwjgl.glfw.GLFW.{GLFW_FALSE, GLFW_TRUE}

object GLFWTypes {
  def bool(flag: Boolean): Int = if (flag) GLFW_TRUE else GLFW_FALSE
}
