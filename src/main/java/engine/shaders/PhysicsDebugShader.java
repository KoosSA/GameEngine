package engine.shaders;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class PhysicsDebugShader extends BaseShader {

	private int loc_projectionMatrix, loc_viewMatrix, loc_transformationMatrix, loc_colour;

	public PhysicsDebugShader() {
		super("pdVertex.glsl", "pdFragment.glsl");
	}

	@Override
	protected void getUniformLocations() {
		loc_projectionMatrix = getUniformLocation("projectionMatrix");
		loc_transformationMatrix = getUniformLocation("transformationMatrix");
		loc_viewMatrix = getUniformLocation("viewMatrix");
		loc_colour = getUniformLocation("colour");
	}

	public void loadProjectionMatrix(Matrix4f projection) {
		loadMatrix4f(loc_projectionMatrix, projection);
	}

	public void loadTransformationMatrix(Matrix4f transformation) {
		loadMatrix4f(loc_transformationMatrix, transformation);
	}

	public void loadViewMatrix(Matrix4f view) {
		loadMatrix4f(loc_viewMatrix, view);
	}

	public void loadColour(Vector3f colour) {
		loadVector3f(loc_colour, colour);
	}
}
