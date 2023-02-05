package engine.lights;

import org.joml.Vector3f;

public class PointLight extends Light {

	private float falloff = 10;

	protected PointLight(Vector3f position, Vector3f colour) {
		super(position, colour);
	}



}
