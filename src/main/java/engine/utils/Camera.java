package engine.utils;

import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import engine.io.GameInput;
import engine.io.IInputHandler;
import engine.io.KeyBindings;
import engine.settings.WindowSettings;

public class Camera implements IInputHandler {

	private Vector3f position = new Vector3f();
	private Vector3f rotation = new Vector3f();
	private float scale = 1;
	private Matrix4f viewMatrix = new Matrix4f();
	private Matrix4f projectionMatrix = new Matrix4f();
	private float fov = 45.0f, nearPlane = 0.001f, farPlane = 1000.0f;

	public Camera() {
		registerInputHandler();
	}

	public void move(float x, float y, float z) {
		position.add(x, y, z);
	}

	private void moveForward(float delta, float direction) {
		position.add(direction * delta * -Math.sin(Math.toRadians(rotation.y())), 0, direction * delta * Math.cos(Math.toRadians(rotation.y())));
	}

	private void moveRight(float delta, float direction) {
		position.add(direction * delta * -Math.cos(Math.toRadians(rotation.y())), 0, direction * delta * -Math.sin(Math.toRadians(rotation.y())));
	}

	private void turn(float angle) {
		rotation.y = MathUtils.loop(rotation.y() + angle * 0.5f, 0, 360);
	}

	public Matrix4f getViewMatrix() {
		viewMatrix.identity();
		viewMatrix.rotateXYZ(Math.toRadians(rotation.x), Math.toRadians(rotation.y), Math.toRadians(rotation.z));
		viewMatrix.translate(position.x(), position.y(), position.z());
		viewMatrix.scale(scale);
		return viewMatrix;
	}

	public Matrix4f getProjectionMatrix() {
		projectionMatrix.identity();
		projectionMatrix.perspective(fov, (float) WindowSettings.width / (float) WindowSettings.height, nearPlane, farPlane);
		return projectionMatrix;
	}

	@Override
	public void handleInput(float delta, GameInput input) {
		turn(input.getMouseDeltaX());
		if (input.isKeyDown(KeyBindings.MOVE_FORWARD))
			moveForward(delta, 1);
		if (input.isKeyDown(KeyBindings.MOVE_BACK))
			moveForward(delta, -1);
		if (input.isKeyDown(KeyBindings.MOVE_RIGHT))
			moveRight(delta, 1);
		if (input.isKeyDown(KeyBindings.MOVE_LEFT))
			moveRight(delta, -1);
		if (input.isKeyDown(KeyBindings.MOVE_UP))
			move(0, -delta, 0);
		if (input.isKeyDown(KeyBindings.MOVE_DOWN))
			move(0, delta, 0);
	}

}
