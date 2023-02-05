package engine.managers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Vector3f;

import engine.lights.AmbientLight;
import engine.lights.DirectionalLight;
import engine.models.Model;
import engine.models.ModelInstance;

public class AssetManager {

	private static Map<Model, List<ModelInstance>> toRender = new HashMap<Model, List<ModelInstance>>();

	private static AmbientLight ambientLight = new AmbientLight(new Vector3f(1,1,1)).setIntensity(0.01f);
	private static DirectionalLight directionalLight = new DirectionalLight(new Vector3f(0.5f,-0.5f,-0.5f), new Vector3f(1)).setIntensity(1);

	public static Map<Model, List<ModelInstance>> getModelsToRender() {
		return toRender;
	}

	public static AmbientLight getAmbientLight() {
		return ambientLight;
	}

	public static DirectionalLight getDirectionalLight() {
		return directionalLight;
	}



}
