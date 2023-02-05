package engine.lights;

import org.joml.Vector3f;

public class AmbientLight extends Light {

	public AmbientLight(Vector3f colour) {
		super(colour);
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
