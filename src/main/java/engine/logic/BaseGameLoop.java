package engine.logic;

import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWWindowSizeCallbackI;
import org.lwjgl.opengl.GL46;

import com.koossa.filesystem.CommonFolders;
import com.koossa.filesystem.Files;
import com.koossa.filesystem.RootFileLocation;
import com.koossa.logger.Log;
import com.koossa.savelib.SaveSystem;
import com.spinyowl.legui.DefaultInitializer;
import com.spinyowl.legui.animation.Animator;
import com.spinyowl.legui.animation.AnimatorProvider;
import com.spinyowl.legui.component.Frame;
import com.spinyowl.legui.listener.processor.EventProcessorProvider;
import com.spinyowl.legui.system.context.CallbackKeeper;
import com.spinyowl.legui.system.context.Context;
import com.spinyowl.legui.system.layout.LayoutManager;
import com.spinyowl.legui.system.renderer.Renderer;

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
	private Renderer guiRenderer;
	private Context guiContext;
	protected Frame frame;
	private DefaultInitializer initializer;
	private Animator guiAnimator;
	protected CallbackKeeper callbackKeeper;
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
		frame = new Frame(WindowSettings.width, WindowSettings.height);
		initializer = new DefaultInitializer(getDisplay().getWindowId(), frame);
		guiRenderer = initializer.getRenderer();
		guiRenderer.initialize();
		guiContext = initializer.getContext();
		guiAnimator = AnimatorProvider.getAnimator();
		callbackKeeper = initializer.getCallbackKeeper();
		cam = new Camera();
		terrainRenderer = new TerrainRenderer(cam);
		staticRenderer = new StaticRenderer(cam);

		callbackKeeper.getChainWindowSizeCallback().add(new GLFWWindowSizeCallbackI() {
			@Override
			public void invoke(long window, int width, int height) {
				onResize(width, height);
			}
		});

		callbackKeeper.getChainKeyCallback().add(new GLFWKeyCallbackI() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (action == GLFW.GLFW_PRESS) {
					input.addKeyPress(key);
				} else if (action == GLFW.GLFW_RELEASE) {
					input.removeKeyPress(key);
				}
			}
		});
		callbackKeeper.getChainCursorPosCallback().add(new GLFWCursorPosCallbackI() {
			@Override
			public void invoke(long window, double xpos, double ypos) {
				input.addMouseMovement(xpos, ypos);
			}
		});

		init();
	}

	public void baseUpdate(float delta) {
		guiContext.updateGlfwWindow();
		guiAnimator.runAnimations();
		initializer.getSystemEventProcessor().processEvents(frame, guiContext);
		initializer.getGuiEventProcessor().processEvents();

		EventProcessorProvider.getInstance().processEvents();
		LayoutManager.getInstance().layout(frame);

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

		guiRenderer.render(frame, guiContext);

		AnimatorProvider.getAnimator().runAnimations();
	}

	public void baseDispose() {
		Log.info(getClass(), "Starting disposal of engine.");

		dispose();

		guiRenderer.destroy();
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
		guiContext.setFramebufferSize(windowSize);
		GL46.glViewport(0, 0, windowSize.x, windowSize.y);
		WindowSettings.height = height;
		WindowSettings.width = width;
		frame.setSize(width, height);
	}

	@Override
	public void handleInput(float delta, GameInput input) {
		if (input.isKeyJustPressed(KeyBindings.FORCE_QUIT)) getDisplay().quit();

	}
}
