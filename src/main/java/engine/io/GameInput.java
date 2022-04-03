package engine.io;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

public class GameInput {
	
	private List<Integer> keysDown = new ArrayList<Integer>();
	private List<Integer> keysJustPressed = new ArrayList<Integer>();
	private Vector2f deltaMouse = new Vector2f();
	private Vector2f prevMouse = new Vector2f();
	private Vector2f currentMouse = new Vector2f();
	private boolean reverseMouseY = true;
	protected static List<IInputHandler> inputHandlers = new ArrayList<IInputHandler>();
	
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
		keysDown.addAll(keysJustPressed);
		keysJustPressed.clear();
		deltaMouse.set(currentMouse.x() - prevMouse.x(), currentMouse.y() - prevMouse.y());
		prevMouse.set(currentMouse);
		
		inputHandlers.forEach(handle -> {
			handle.handleInput(delta, this);
		});
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

}
