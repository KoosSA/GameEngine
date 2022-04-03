package engine.utils;

import org.joml.Vector4f;

public class Material {
	
	private Texture diffuse;
	private Texture normal;
	private Texture specular;
	
	private Vector4f colour = new Vector4f();
	
	private boolean useTextures = true;

	public Texture getDiffuse() {
		return diffuse;
	}

	public void setDiffuse(Texture diffuse) {
		this.diffuse = diffuse;
	}

	public Texture getNormal() {
		return normal;
	}

	public void setNormal(Texture normal) {
		this.normal = normal;
	}

	public Texture getSpecular() {
		return specular;
	}

	public void setSpecular(Texture specular) {
		this.specular = specular;
	}

	public Vector4f getColour() {
		return colour;
	}

	public void setColour(float r, float g, float b, float a) {
		this.colour.set(r, g, b, a);
	}

	public boolean isUseTextures() {
		return useTextures;
	}

	public void setUseTextures(boolean useTextures) {
		this.useTextures = useTextures;
	}
	
	

}
