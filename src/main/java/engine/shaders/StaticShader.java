package engine.shaders;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL15;

import engine.utils.Material;

public class StaticShader extends BaseShader {
	
	private int loc_projectionMatrix, loc_viewMatrix, loc_transformationMatrix, loc_useTextures, loc_baseColour;

	public StaticShader() {
		super("staticVertex.glsl", "staticFragment.glsl");
	}

	@Override
	protected void getUniformLocations() {
		loc_projectionMatrix = getUniformLocation("projectionMatrix");
		loc_transformationMatrix = getUniformLocation("transformationMatrix");
		loc_viewMatrix = getUniformLocation("viewMatrix");
		loc_useTextures = getUniformLocation("useTextures");
		loc_baseColour = getUniformLocation("baseColour");
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

	public void loadMaterial(Material material) {
		loadBoolean(loc_useTextures, material.isUseTextures());
		loadVector4f(loc_baseColour, material.getColour());
		
		if (material.getDiffuse() != null) {
			GL15.glActiveTexture(GL15.GL_TEXTURE0);
			GL15.glBindTexture(GL15.GL_TEXTURE_2D, material.getDiffuse().getId());
		}
	}
}
