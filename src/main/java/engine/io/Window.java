package engine.io;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL46;

import com.koossa.logger.Log;

import engine.logic.BaseGameLoop;
import engine.settings.WindowSettings;

public class Window {

	private long window = 0;
	private BaseGameLoop loop;
	private GameInput input;

	public Window(BaseGameLoop gameloop) {
		this.loop = gameloop;
	}

	public void create() {
		Log.info(getClass(), "Start window creation.");


		if (!GLFW.glfwInit()) {
			Log.error(getClass(), "Failed to initialise GLFW.");
			dispose();
			return;
		}

		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, WindowSettings.aa_samples);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 6);


		long primaryMonitor = GLFW.glfwGetPrimaryMonitor();
		GLFWVidMode vidMode = GLFW.glfwGetVideoMode(primaryMonitor);
		if (WindowSettings.width <= 0 || WindowSettings.height <= 0) {
			Log.error(getClass(), "Invalid window width or height - Changing to fullscreen mode.");
			WindowSettings.fullscreen = true;
		}
		if (WindowSettings.fullscreen) {
			WindowSettings.width = vidMode.width();
			WindowSettings.height = vidMode.height();
		}

		if (WindowSettings.fullscreen) {
			window = GLFW.glfwCreateWindow(WindowSettings.width, WindowSettings.height, WindowSettings.title, primaryMonitor, 0);
		} else {
			window = GLFW.glfwCreateWindow(WindowSettings.width, WindowSettings.height, WindowSettings.title, 0, 0);
		}
		if (window == 0) {
			Log.error(getClass(), "Failed to create window.");
			dispose();
		}

		GLFW.glfwMakeContextCurrent(window);

		GL.createCapabilities();

		GL46.glEnable(GL46.GL_CULL_FACE);
		GL46.glCullFace(GL46.GL_BACK);

		if (WindowSettings.vsync) {
			GLFW.glfwSwapInterval(1);
		} else {
			GLFW.glfwSwapInterval(0);
		}

		Log.info(getClass(), "Window created.");

		input = new GameInput(window);
		loop.baseInit(input);

		input.hideCursor();

		GLFW.glfwShowWindow(window);

		Log.info(getClass(), "Starting game loop.");
		loop();

		dispose();
	}

	private void loop() {

		double tft = 1.000 / WindowSettings.targetFPS;
		double pt = GLFW.glfwGetTime();
		double delta = 0.0;

		while (!GLFW.glfwWindowShouldClose(window)) {
			delta = GLFW.glfwGetTime() - pt;
			if (delta >= tft) {
				update((float) delta);
				render();
				pt = GLFW.glfwGetTime();
			}

		}

	}

	private void update(float delta) {
		input.update(delta);
		GLFW.glfwPollEvents();
		loop.baseUpdate(delta);
	}

	private void render() {
		loop.baseRender();
		GLFW.glfwSwapBuffers(window);
	}

	private void dispose() {
		Log.info(getClass(), "Disposing window.");

		loop.baseDispose();
		GLFW.glfwDestroyWindow(window);
		GLFW.glfwTerminate();
	}

	public long getWindowId() {
		return window;
	}

	public void quit() {
		GLFW.glfwSetWindowShouldClose(window, true);
	}

}
