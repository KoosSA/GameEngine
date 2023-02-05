package engine.shaders;

import org.lwjgl.opengl.GL46;

import engine.utils.Material;

public class TerrainShader extends BaseShader {

	private int loc_useTextures, loc_baseColour;

	public TerrainShader() {
		super("terrainVertex.glsl", "terrainFragment.glsl");
	}

	@Override
	protected void getUniformLocations() {
		loc_useTextures = getUniformLocation("useTextures");
		loc_baseColour = getUniformLocation("baseColour");
	}

	public void loadMaterial(Material mat) {
		loadBoolean(loc_useTextures, mat.isUseTextures());
		loadVector4f(loc_baseColour, mat.getColour());

		if (mat.isUseTextures()) {
			GL46.glActiveTexture(GL46.GL_TEXTURE0);
			GL46.glBindTexture(GL46.GL_TEXTURE_2D, mat.getDiffuse().getId());
		}
	}

}
