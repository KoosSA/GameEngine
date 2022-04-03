package engine.terrain;

import com.koossa.savelib.ISavable;

public class Biome implements ISavable {

	private String name = "Grasslands";
	private String groundTexture = "grass.png";
	private float treeSpawnChance = 1.0f;
	private float maxTrees = 100;

	public String getName() {
		return name;
	}
	public String getGroundTexture() {
		return groundTexture;
	}
	public float getTreeSpawnChance() {
		return treeSpawnChance;
	}
	public float getMaxTrees() {
		return maxTrees;
	}



}


