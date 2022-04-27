package engine.terrain;

import org.joml.Vector3f;

import engine.models.RawModel;
import engine.utils.Loader;
import engine.utils.Material;

public class Chunk {

	private RawModel rawmodel;
	private Material material;
	private Biome biome;
	private Vector3f position = new Vector3f();


	public Chunk(int vertex_count, int size) {
		rawmodel = generateTerrain(vertex_count, size);
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

	public Biome getBiome() {
		return biome;
	}

	public void setPosition(float x, float y, float z) {
		this.position.set(x, y, z);
	}

	public Vector3f getPosition() {
		return position;
	}

	private RawModel generateTerrain(int vertex_count, int size) {
		int count = vertex_count * vertex_count;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count*2];
		int[] indices = new int[6*(vertex_count-1)*(vertex_count-1)];
		int vertexPointer = 0;
		for(int i=0;i<vertex_count;i++){
			for(int j=0;j<vertex_count;j++){
				vertices[vertexPointer*3] = (float)j/((float)vertex_count - 1) * size;
				vertices[vertexPointer*3+1] = 0;
				vertices[vertexPointer*3+2] = (float)i/((float)vertex_count - 1) * size;
				normals[vertexPointer*3] = 0;
				normals[vertexPointer*3+1] = 1;
				normals[vertexPointer*3+2] = 0;
				textureCoords[vertexPointer*2] = (float)j/((float)vertex_count - 1);
				textureCoords[vertexPointer*2+1] = (float)i/((float)vertex_count - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for(int gz=0;gz<vertex_count-1;gz++){
			for(int gx=0;gx<vertex_count-1;gx++){
				int topLeft = (gz*vertex_count)+gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz+1)*vertex_count)+gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return Loader.loadModelData(vertices, textureCoords, normals, indices);
	}

}
