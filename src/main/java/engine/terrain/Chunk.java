package engine.terrain;

import org.joml.Vector3f;

import com.jme3.bullet.collision.shapes.HeightfieldCollisionShape;
import com.jme3.bullet.objects.PhysicsRigidBody;

import engine.models.RawModel;
import engine.utils.Loader;
import engine.utils.Material;

public class Chunk {

	private RawModel rawmodel;
	private Material material;
	private Biome biome;
	private Vector3f position = new Vector3f();
	private PhysicsRigidBody rigidBody;
	private float size;
	private static HeightGenerator generator = new HeightGenerator();
	private float[] vertices;
	private float[] normals;
	private float[] textureCoords;
	private float[] heightmap;
	private int[] indices;

	public Chunk(int vertex_count, int size, int right, int up) {
		this.size = size;
		generateTerrainData(vertex_count, size, right, up);
		setPosition(right * size, 0, up * size);
		material = new Material();
		material.setUseTextures(true);
	}

	public void setBiome(Biome biome) {
		this.biome = biome;
		material.setDiffuse(Loader.getTexture(biome.getGroundTexture()));
		//material.setUseTextures(false);
		//material.setColour(0, 0, 1, 1);
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
		//rigidBody.setPhysicsLocation(rigidBody.getPhysicsLocation(null).set(x + size / 2f - (size / (float) vertex_count) / 2f, y, z + size/2f - (size / (float) vertex_count) / 2f));
		rigidBody.setPhysicsLocation(rigidBody.getPhysicsLocation(null).set(x + size / 2f, y, z + size/2f));
		//rigidBody.setPhysicsLocation(rigidBody.getPhysicsLocation(null).set(x, y, z));
	}

	public Vector3f getPosition() {
		return position;
	}

	private void generateTerrainData(int vertex_count, int size, int right, int up) {
		//Log.info(this, "Generating new chunk.");
		int count = vertex_count * vertex_count;
		vertices = new float[count * 3];
		normals = new float[count * 3];
		textureCoords = new float[count * 2];
		heightmap = new float[count];
		indices = new int[6 * (vertex_count - 1) * (vertex_count - 1)];
		int vertexPointer = 0;
		float physicsscale = 0;
		Vector3f normal = new Vector3f();

		for (int i = 0; i < vertex_count; i++) {
			for (int j = 0; j < vertex_count; j++) {
				vertices[vertexPointer * 3] = (float) j / ((float) vertex_count - 1) * size;
				vertices[vertexPointer * 3 + 1] = getHeight(j + right * (vertex_count-1), i + up * (vertex_count-1));
				vertices[vertexPointer * 3 + 2] = (float) i / ((float) vertex_count - 1) * size;
				heightmap[vertexPointer] = vertices[vertexPointer * 3 + 1];
				normal = calculateNormal(j, i, normal);
				normals[vertexPointer * 3] = normal.x();
				normals[vertexPointer * 3 + 1] = normal.y();
				normals[vertexPointer * 3 + 2] = normal.z();
				textureCoords[vertexPointer * 2] = (float) j / ((float) vertex_count - 1);
				textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) vertex_count - 1);
				vertexPointer++;
			}
		}

		int pointer = 0;
		for (int gz = 0; gz < vertex_count - 1; gz++) {
			for (int gx = 0; gx < vertex_count - 1; gx++) {
				int topLeft = (gz * vertex_count) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * vertex_count) + gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}

		physicsscale = vertices[3];
		HeightfieldCollisionShape cs = new HeightfieldCollisionShape(vertex_count, vertex_count, heightmap,
			new Vector3f(physicsscale, 1f, physicsscale), 1, false, true, false, false);
		rigidBody = new PhysicsRigidBody(cs);
		rigidBody.setMass(0);
	}

	public RawModel genRawModel() {
		rawmodel = Loader.loadModelData(vertices, textureCoords, normals, indices);
		return rawmodel;
	}

	private Vector3f calculateNormal(int x, int z, Vector3f normal) {
		float heightL = getHeight(x-1, z);
		float heightR = getHeight(x+1, z);
		float heightD = getHeight(x, z-1);
		float heightU = getHeight(x, z+1);
		normal.set(heightL - heightR, 2.0f, heightD - heightU);
		normal.normalize();
		return normal;
	}

	private float getHeight(int x, int z) {
		return generator.generateHeight(x, z);
	}

	public PhysicsRigidBody getRigidBody() {
		return rigidBody;
	}

}
