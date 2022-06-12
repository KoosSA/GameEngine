package engine.managers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import engine.models.Model;
import engine.models.ModelInstance;

public class AssetManager {

	private static Map<Model, List<ModelInstance>> toRender = new HashMap<Model, List<ModelInstance>>();

	public static Map<Model, List<ModelInstance>> getModelsToRender() {
		return toRender;
	}



}
