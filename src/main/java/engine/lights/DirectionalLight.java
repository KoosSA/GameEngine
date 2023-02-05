package engine.lights;

import org.joml.Vector3f;

public class DirectionalLight extends Light{

	public DirectionalLight(Vector3f direction, Vector3f colour) {
		super(direction, colour);
	}

	public void setDirection(Vector3f direction) {
		setPosition(direction);
	}

	public Vector3f getDirection() {
		return getPosition();
	}

	@Override
	public Vector3f getColour() {
		return super.getColour();
	}

	@Override
	public void setColour(Vector3f colour) {
		super.setColour(colour);
	}

}
