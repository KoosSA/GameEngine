package engine.terrain;

import engine.models.RawModel;
import engine.utils.Loader;
import engine.utils.Material;

public class Chunk {

	private RawModel rawmodel;
	private Material material;
	private Biome biome;


	public Chunk() {
		rawmodel = TerrainGenerator.generateTerrain();
		material = new Material();
		material.setUseTextures(true);
	}

	public void setBiome(Biome biome) {
		this.biome = biome;
		material.setDiffuse(Loader.getTexture(biome.getGroundTexture()));
	}

	public RawModel getRawmodel() {
		return rawmodel;
	}

	public Material getMaterial() {
		return material;
	}



}
