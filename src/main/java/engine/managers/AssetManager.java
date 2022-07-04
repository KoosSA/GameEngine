package engine.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import engine.lights.Light;
import engine.models.Model;
import engine.models.ModelInstance;

public class AssetManager {

	private static Map<Model, List<ModelInstance>> toRender = new HashMap<Model, List<ModelInstance>>();
	private static List<Light> lights = new ArrayList<>();

	public static Map<Model, List<ModelInstance>> getModelsToRender() {
		return toRender;
	}

	public static List<Light> getLights() {
		return lights;
	}



}
