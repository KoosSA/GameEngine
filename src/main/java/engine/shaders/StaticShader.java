package engine.shaders;

import org.lwjgl.opengl.GL46;

import engine.utils.Material;

public class StaticShader extends BaseShader {

	private int loc_useTextures, loc_baseColour;
	private int loc_reflectivity, loc_shineDampener;

	public StaticShader() {
		super("staticVertex.glsl", "staticFragment.glsl");
	}

	@Override
	protected void getUniformLocations() {
		loc_useTextures = getUniformLocation("useTextures");
		loc_baseColour = getUniformLocation("baseColour");
		loc_reflectivity = getUniformLocation("reflectivity");
		loc_shineDampener = getUniformLocation("shineDampener");
	}


	public void loadMaterial(Material material) {
		loadBoolean(loc_useTextures, material.isUseTextures());
		loadVector4f(loc_baseColour, material.getColour());
		loadFloat(loc_reflectivity, material.getReflectivity());
		loadFloat(loc_shineDampener, material.getShineDamper());

		if (material.getDiffuse() != null) {
			GL46.glActiveTexture(GL46.GL_TEXTURE0);
			GL46.glBindTexture(GL46.GL_TEXTURE_2D, material.getDiffuse().getId());
		}
	}

}
