package engine.utils;

import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import engine.io.GameInput;
import engine.io.IInputHandler;
import engine.io.KeyBindings;
import engine.settings.WindowSettings;
import engine.terrain.TerrainGenerator;

public class Camera implements IInputHandler {

	private Vector3f position = new Vector3f();
	private Quaternionf rotation = new Quaternionf();
	private float scale = 1;
	private Matrix4f viewMatrix = new Matrix4f();
	private Matrix4f projectionMatrix = new Matrix4f();
	private float fov = 45.0f, nearPlane = 0.001f, farPlane = 1000.0f;
	private boolean active = true;
	private float speed = 1;
	private Vector3f forward = new Vector3f(0, 0, -1);
	private Vector3f right = new Vector3f(-1, 0, 0);
	private Vector3f direction = new Vector3f(0, 0, -1);
	private Vector3f point = new Vector3f();

	public Camera() {
		registerInputHandler();
	}

	public void move(float x, float y, float z) {
		position.add(x, y, z);
		TerrainGenerator.updateTerrianFromCam(position);
	}

	private void moveForward(float delta, float direction) {
		position.add(getForward().x() * direction * delta * speed, 0, getForward().z * direction * delta * speed);
		TerrainGenerator.updateTerrianFromCam(position);
	}

	private void moveRight(float delta, float direction) {
		position.add(getRight().x() * speed * delta * -direction, 0, getRight().z() * speed * delta * -direction);
		TerrainGenerator.updateTerrianFromCam(position);
	}

	public void turn(float angle) {
		// rotation.y = MathUtils.loop(rotation.y() + angle * 0.5f, 0, 360);
		rotation.rotateY(Math.toRadians(angle));
		forward.rotateY(Math.toRadians(-angle));
		right.rotateY(Math.toRadians(-angle));
		forward.normalize();
		right.normalize();
	}

	public void pitch(float angle) {
		// rotation.x = MathUtils.clamp(rotation.x() - angle * 0.5f, -90, 90);
		rotation.rotateLocalX(Math.toRadians(-angle));

	}

	public Matrix4f getViewMatrix() {
		viewMatrix.identity();
		// viewMatrix.rotateXYZ(Math.toRadians(rotation.x), Math.toRadians(rotation.y),
		// Math.toRadians(rotation.z));
		viewMatrix.rotate(rotation);
		viewMatrix.translate(-position.x(), -position.y(), -position.z());
		viewMatrix.scale(scale);
		return viewMatrix;
	}

	public Matrix4f getProjectionMatrix() {
		projectionMatrix.identity();
		projectionMatrix.perspective(fov, (float) WindowSettings.width / (float) WindowSettings.height, nearPlane,
				farPlane);
		return projectionMatrix;
	}

	@Override
	public void handleInput(float delta, GameInput input) {
		if (!active)
			return;
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
			move(0, delta * speed, 0);
		if (input.isKeyDown(KeyBindings.MOVE_DOWN))
			move(0, -delta * speed, 0);
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

	public Vector3f getPosition() {
		return position;
	}

	public Quaternionf getRotation() {
		return rotation;
	}

	public Vector3f getDirection() {
		direction.set(0, 0, -1);
		rotation.transformInverse(direction);
		direction.normalize();
		return direction;
	}

	private Vector3f getForward() {
		return forward;
	}

	private Vector3f getRight() {
		return right;
	}

	public Vector3f getPointInFrontOfCam(float distance) {
		position.add(getDirection().mul(distance), point);
		return point;
	}



}
