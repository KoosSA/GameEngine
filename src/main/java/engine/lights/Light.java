package engine.lights;

import org.joml.Vector3f;

public abstract class Light {

	private Vector3f position;
	private Vector3f colour;
	private float intensity;

	protected Light(Vector3f position, Vector3f colour) {
		this.position = position;
		this.colour = colour;
	}

	protected Light(Vector3f colour) {
		this.colour = colour;
		this.position = new Vector3f();
	}

	protected Vector3f getPosition() {
		return position;
	}

	protected void setPosition(Vector3f position) {
		this.position = position;
	}

	protected Vector3f getColour() {
		return colour;
	}

	protected void setColour(Vector3f colour) {
		this.colour = colour;
	}

	public float getIntensity() {
		return intensity;
	}

	@SuppressWarnings("unchecked")
	public <T> T setIntensity(float intensity) {
		this.intensity = intensity;
		return (T) this;
	}



}
