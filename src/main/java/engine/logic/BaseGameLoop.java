package engine.logic;

import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL46;

import com.koossa.filesystem.CommonFolders;
import com.koossa.filesystem.Files;
import com.koossa.filesystem.RootFileLocation;
import com.koossa.logger.Log;
import com.koossa.savelib.SaveSystem;

import engine.io.GameInput;
import engine.io.IInputHandler;
import engine.io.KeyBindings;
import engine.io.Window;
import engine.managers.AssetManager;
import engine.renderers.StaticRenderer;
import engine.renderers.TerrainRenderer;
import engine.settings.WindowSettings;
import engine.utils.Camera;
import engine.utils.Loader;

public abstract class BaseGameLoop extends Thread implements IInputHandler {

	private Window display;
	private Vector2i windowSize = new Vector2i();
	protected TerrainRenderer terrainRenderer;
	protected StaticRenderer staticRenderer;
	protected Camera cam;

	public BaseGameLoop() {
		super();
		setName("main thread");
	}

	@Override
	public void start() {
		Files.init("Assets", RootFileLocation.LOCAL);
		Log.setMaxLogFiles(10);
		Log.init(Files.getCommonFolder(CommonFolders.Logs), true);
		Log.info(getClass(), "Starting the engine.");
		SaveSystem.init(Files.getRootFolder(), Files.getFolder("Data"));
		display = new Window(this);
		display.create();
	}

	public void baseInit(GameInput input) {
		registerInputHandler();

		cam = new Camera();
		terrainRenderer = new TerrainRenderer(cam);
		staticRenderer = new StaticRenderer(cam);

		GLFW.glfwSetWindowSizeCallback(display.getWindowId(), new GLFWWindowSizeCallback() {
			@Override
			public void invoke(long window, int width, int height) {
				onResize(width, height);
			}
		});

		GLFW.glfwSetKeyCallback(display.getWindowId(), new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (action == GLFW.GLFW_PRESS) {
					input.addKeyPress(key);
				} else if (action == GLFW.GLFW_RELEASE) {
					input.removeKeyPress(key);
				}
			}
		});

		GLFW.glfwSetCursorPosCallback(display.getWindowId(), new GLFWCursorPosCallbackI() {
			@Override
			public void invoke(long window, double xpos, double ypos) {
				input.addMouseMovement(xpos, ypos);
			}
		});

		init();
	}

	public void baseUpdate(float delta) {

		update(delta);
	}

	public void baseRender() {
		GL46.glClearColor(0.5f, 0.5f, 0.5f, 1);
		GL46.glClear(GL46.GL_COLOR_BUFFER_BIT | GL46.GL_STENCIL_BUFFER_BIT | GL46.GL_DEPTH_BUFFER_BIT);

		GL46.glEnable(GL46.GL_DEPTH_TEST);
		GL46.glEnable(GL46.GL_CULL_FACE);
		GL46.glCullFace(GL46.GL_BACK);

		terrainRenderer.render();
		staticRenderer.render(AssetManager.getModelsToRender());

		render();

	}

	public void baseDispose() {
		Log.info(getClass(), "Starting disposal of engine.");

		dispose();

		Loader.dispose();
		Log.dispose();
	}

	protected abstract void init();
	protected abstract void update(float delta);
	protected abstract void render();
	protected abstract void dispose();

	public Window getDisplay() {
		return display;
	}

	protected void onResize(int width, int height) {
		windowSize.set(width, height);
		GL46.glViewport(0, 0, windowSize.x, windowSize.y);
		WindowSettings.height = height;
		WindowSettings.width = width;
	}

	@Override
	public void handleInput(float delta, GameInput input) {
		if (input.isKeyJustPressed(KeyBindings.FORCE_QUIT)) getDisplay().quit();

	}
}
