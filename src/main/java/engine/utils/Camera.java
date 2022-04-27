package engine.utils;

import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import engine.io.GameInput;
import engine.io.IInputHandler;
import engine.io.KeyBindings;
import engine.settings.WindowSettings;
import engine.terrain.TerrainGenerator;

public class Camera implements IInputHandler {

	private Vector3f position = new Vector3f();
	private Vector3f rotation = new Vector3f();
	private float scale = 1;
	private Matrix4f viewMatrix = new Matrix4f();
	private Matrix4f projectionMatrix = new Matrix4f();
	private float fov = 45.0f, nearPlane = 0.001f, farPlane = 1000.0f;
	private boolean active = true;
	private float speed = 1;

	public Camera() {
		registerInputHandler();
	}

	public void move(float x, float y, float z) {
		position.add(x, y, z);
		TerrainGenerator.updateTerrianFromCam(position);
	}

	private void moveForward(float delta, float direction) {
		position.add(direction * delta * speed * -Math.sin(Math.toRadians(rotation.y())), 0, direction * delta * speed * Math.cos(Math.toRadians(rotation.y())));
		TerrainGenerator.updateTerrianFromCam(position);
	}

	private void moveRight(float delta, float direction) {
		position.add(direction * delta * speed * -Math.cos(Math.toRadians(rotation.y())), 0, direction * delta * speed * -Math.sin(Math.toRadians(rotation.y())));
		TerrainGenerator.updateTerrianFromCam(position);
	}

	private void turn(float angle) {
		rotation.y = MathUtils.loop(rotation.y() + angle * 0.5f, 0, 360);
	}

	private void pitch(float angle) {
		rotation.x = MathUtils.clamp(rotation.x() - angle * 0.5f, -90, 90);
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
		if (!active) return;
		turn(input.getMouseDeltaX());
		pitch(input.getMouseDeltaY());
		if (input.isKeyDown(KeyBindings.MOVE_FORWARD))
			moveForward(delta, 1);
		if (input.isKeyDown(KeyBindings.MOVE_BACK))
			moveForward(delta, -1);
		if (input.isKeyDown(KeyBindings.MOVE_RIGHT))
			moveRight(delta, 1);
		if (input.isKeyDown(KeyBindings.MOVE_LEFT))
			moveRight(delta, -1);
		if (input.isKeyDown(KeyBindings.MOVE_UP))
			move(0, -delta * speed, 0);
		if (input.isKeyDown(KeyBindings.MOVE_DOWN))
			move(0, delta * speed, 0);
		if (input.isKeyDown(KeyBindings.SPRINT)) {
			speed = 20;
		} else {
			speed = 1;
		}
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isActive() {
		return active;
	}

}
