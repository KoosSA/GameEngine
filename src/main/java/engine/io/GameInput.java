package engine.io;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import engine.settings.WindowSettings;

public class GameInput {

	private List<Integer> keysDown = new ArrayList<Integer>();
	private List<Integer> keysJustPressed = new ArrayList<Integer>();
	private Vector2f deltaMouse = new Vector2f();
	private Vector2f prevMouse = new Vector2f();
	private Vector2f currentMouse = new Vector2f();
	private boolean reverseMouseY = true;
	protected static List<IInputHandler> inputHandlers = new ArrayList<IInputHandler>();
	private boolean cursorHidden = false;
	private long window;

	public GameInput(long window) {
		this.window = window;
	}

	public boolean isKeyDown(int key) {
		return keysDown.contains(key);
	}

	public boolean isKeyJustPressed(int key) {
		return keysJustPressed.contains(key);
	}

	public float getMouseDeltaX() {
		return deltaMouse.x();
	}

	public float getMouseDeltaY() {
		return (reverseMouseY ? deltaMouse.y() * -1 : deltaMouse.y());
	}

	public void update(float delta) {
		inputHandlers.forEach(handle -> {
			handle.handleInput(delta, this);
		});
		
		keysDown.addAll(keysJustPressed);
		keysJustPressed.clear();
		deltaMouse.set(currentMouse.x() - prevMouse.x(), currentMouse.y() - prevMouse.y());
		prevMouse.set(currentMouse);
	}

	public void addKeyPress(int key) {
		boolean down = keysDown.contains(key);
		boolean press = keysJustPressed.contains(key);
		if (!down && !press) {
			keysJustPressed.add(key);
		}
	}

	public void removeKeyPress(int key) {
		boolean down = keysDown.contains(key);
		boolean press = keysJustPressed.contains(key);
		if (down) {
			keysDown.remove((Object) key);
		}
		if (press) {
			keysJustPressed.remove((Object) key);
		}
	}

	public void addMouseMovement(double x, double y) {
		currentMouse.set(x, y);
	}

	public void setPrevMouse(float x, float y) {
		prevMouse.set(x, y);
	}

	public void hideCursor() {
		if (cursorHidden) return;
		GLFW.glfwSetCursorPos(window, 0, 0);
		GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
		cursorHidden = true;
	}

	public void showCursor() {
		if (!cursorHidden) return;
		GLFW.glfwSetCursorPos(window, WindowSettings.width / 2, WindowSettings.height / 2);
		//GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
		cursorHidden = false;
	}

	public void toggleCursor() {
		if (cursorHidden) {
			showCursor();
		} else {
			hideCursor();
		}
	}

}
