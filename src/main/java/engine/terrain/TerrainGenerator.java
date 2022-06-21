package engine.terrain;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.joml.Math;
import org.joml.Vector3f;

import com.koossa.filesystem.Files;
import com.koossa.logger.Log;
import com.koossa.savelib.SaveSystem;

import engine.physics.Physics;

public class TerrainGenerator {

	public static final int VERTEX_COUNT = 128;
	public static final float SIZE = 400;

	private static Map<String, Chunk> chunkList = new HashMap<String, Chunk>();
	private static Map<String, Biome> availableBiomes = new HashMap<String, Biome>();
	private static List<String> names = new ArrayList<String>();
	private static Random r = new Random();
	private static boolean valid = false;
	private static int chunkNumberInViewdistance = 3;
	private static List<Chunk> toRender = new ArrayList<Chunk>();
	private static List<Chunk> prevRender = new ArrayList<Chunk>();
	private static Physics physics;
	private static int prevUp = 0, prevRight = 0;

	public static void updateTerrianFromCam(Vector3f position) {
		if (!valid) return;
		int up = (int) Math.floor(position.z() / SIZE);
		int right = (int) Math.floor(position.x() / SIZE);

		if (prevUp != up || prevRight != right) {

			updateTerrain(up, right);

			prevUp = up;
			prevRight = right;
		}

	}

	private static void updateTerrain(int up, int right) {
		prevRender.clear();
		prevRender.addAll(toRender);
		toRender.clear();

		for (int x = -chunkNumberInViewdistance; x <= chunkNumberInViewdistance; x++) {
			for (int z = -chunkNumberInViewdistance; z <= chunkNumberInViewdistance; z++) {
				Chunk c = getChunk(up + x, right + z);
				if (!toRender.contains(c)) toRender.add(c);
			}
		}

		//Chunk c = getChunk(up, right);
		//if (!toRender.contains(c)) toRender.add(c);



		prevRender.forEach(ch -> {
			if (!toRender.contains(ch)) {
				physics.removeObjectFromPhysicsWorld(ch.getRigidBody());
			}
		});
		toRender.forEach(ch -> {
			if (!prevRender.contains(ch)) {
				physics.addObjectToPhysicsWorld(ch.getRigidBody());
			}
		});
		if (physics.isDebug()) {
			Chunk c1 = getChunk(up, right);
			physics.addToDebugRenderer(c1.getRigidBody());
			Chunk pc = getChunk(prevUp, prevRight);
			physics.removeFromDebugRenderer(pc.getRigidBody());
		}
	}

	public static void init(Physics physics) {
		TerrainGenerator.physics = physics;
		if (availableBiomes.keySet().size() <= 0) loadBiomes();
		if (names.size() > 0) valid = true;
		updateTerrain(0, 0);
	}

	private static Chunk getChunk(int up, int right) {
		String id = right + "_" + up;
		Chunk c = chunkList.getOrDefault(id, null);
		if (c != null) {
			return c;
		}
		//Log.debug(TerrainGenerator.class, "Chunk " + right + ", " + up + " does not exist. Creating new chunk.");
		return generateChunk(up, right, id);
	}

	private static Chunk generateChunk(int up, int right, String id) {
		Chunk c = new Chunk(VERTEX_COUNT, SIZE, right, up);
		c.setBiome(availableBiomes.get(names.get(r.nextInt(names.size()))));
		c.setPosition(right * SIZE, 0, up * SIZE);
		chunkList.put(id, c);
		return c;
	}

	private static void loadBiomes() {
		File folder = Files.getFolder("Biomes");
		String[] files = folder.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".json");
			}
		});
		for (int i = 0; i < files.length; i++) {
			Biome b = SaveSystem.load(Biome.class, false, "Biomes", files[i]);
			availableBiomes.putIfAbsent(b.getName(), b);
		}
		names.addAll(availableBiomes.keySet());
	}

	public static List<Chunk> getToRender() {
		return toRender;
	}

	public static Biome getBiome(String name) {
		if (availableBiomes.containsKey(name)) {
			return availableBiomes.get(name);
		}
		Log.error(TerrainGenerator.class, "Biome does not exist: " + name);
		return null;
	}

	public static List<String> getBiomeNames() {
		return names;
	}



}
