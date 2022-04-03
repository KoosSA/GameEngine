package engine.models;

import engine.utils.Material;

public class Mesh {
	
	private RawModel rawModel;
	private Material material;
	
	public Mesh(RawModel rawModel, Material material) {
		this.rawModel = rawModel;
		this.material = material;
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public RawModel getRawModel() {
		return rawModel;
	}

}
