package engine.terrain;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.koossa.filesystem.Files;
import com.koossa.savelib.SaveSystem;

import engine.models.RawModel;
import engine.utils.Loader;

public class TerrainGenerator {

	public static final int VERTEX_COUNT = 128;
	public static final int SIZE = 800;

	private static List<Chunk> chunkList = new ArrayList<Chunk>();
	private static Map<String, Biome> availableBiomes = new HashMap<String, Biome>();
	private static List<String> names = new ArrayList<String>();
	private static Random r = new Random();

	public static void generate() {
		if (availableBiomes.keySet().size() <= 0) loadBiomes();

		generateChunk();
	}

	private static void loadBiomes() {
		File folder = Files.getFolder("Data/Biomes");
		String[] files = folder.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".json");
			}
		});
		for (int i = 0; i < files.length; i++) {
			Biome b = SaveSystem.load(Biome.class, true, "Biomes", files[i]);
			availableBiomes.putIfAbsent(b.getName(), b);
		}
		names.addAll(availableBiomes.keySet());
	}

	private static void generateChunk() {

		Chunk c = new Chunk();
		c.setBiome(availableBiomes.get(names.get(r.nextInt(names.size()))));
		chunkList.add(c);
	}

	protected static RawModel generateTerrain() {
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count*2];
		int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
		int vertexPointer = 0;
		for(int i=0;i<VERTEX_COUNT;i++){
			for(int j=0;j<VERTEX_COUNT;j++){
				vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
				vertices[vertexPointer*3+1] = 0;
				vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
				normals[vertexPointer*3] = 0;
				normals[vertexPointer*3+1] = 1;
				normals[vertexPointer*3+2] = 0;
				textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
				textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for(int gz=0;gz<VERTEX_COUNT-1;gz++){
			for(int gx=0;gx<VERTEX_COUNT-1;gx++){
				int topLeft = (gz*VERTEX_COUNT)+gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
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

	public static List<Chunk> getChunkList() {
		return chunkList;
	}

}
