package engine.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL30;

import com.koossa.filesystem.CommonFolders;
import com.koossa.filesystem.Files;

import engine.models.Model;
import engine.models.RawModel;

public class Loader {

	private static Map<String, Texture> textureMap = new HashMap<>();
	private static Map<String, Model> modelMap = new HashMap<String, Model>();
	private static List<Integer> vbos = new ArrayList<>();

	public static Texture getTexture(String textureName) {
		if (textureMap.containsKey(textureName)) {
			return textureMap.get(textureName);
		}
		Texture t = new Texture(textureName);
		textureMap.put(textureName, t);
		return t;
	}

	public static void loadAllTextures() {
		String[] files = Files.getCommonFolder(CommonFolders.Textures).list();
		if (files == null) return;
		for (int i = 0; i < files.length; i++) {
			String file = files[i];
			if (file.endsWith(".png") || file.endsWith(".jpg")) {
				getTexture(file);
			}
		}
	}

	public static Model getModel(String name) {
		if (modelMap.containsKey(name)) {
			return modelMap.get(name);
		}
		Model m = new Model(name);
		modelMap.put(name, m);
		return m;
	}

	public static void loadAllModels() {
		String[] files = Files.getCommonFolder(CommonFolders.Models).list();
		if (files == null) return;
		for (int i = 0; i < files.length; i++) {
			String file = files[i];
			if (file.endsWith(".obj") || file.endsWith(".fbx")) {
				getModel(file);
			}
		}
	}



	public static void dispose() {
		textureMap.values().forEach(texture -> {
			texture.dispose();
		});
		textureMap.clear();
		modelMap.values().forEach(model -> {
			model.dispose();
		});
		vbos.forEach(vbo ->{
			GL30.glDeleteBuffers(vbo);
		});
	}

	public static RawModel loadModelData(float[] vertices, float[] texCoords, float[] normals, int[] indices) {
		int vao = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vao);
		storeFloatData(0, 3, vertices);
		storeFloatData(1, 2, texCoords);
		storeFloatData(2, 3, normals);
		storeIndices(indices);
		GL30.glEnableVertexAttribArray(0);
		GL30.glEnableVertexAttribArray(1);
		GL30.glEnableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
		return new RawModel(vao, indices.length);
	}

	private static void storeIndices(int[] indices) {
		int id = GL30.glGenBuffers();
		GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, id);
		GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, indices, GL30.GL_STATIC_DRAW);
		vbos.add(id);
	}

	private static void storeFloatData(int index, int size, float[] data) {
		int vbo = GL30.glGenBuffers();
		vbos.add(vbo);
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vbo);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, data, GL30.GL_STATIC_DRAW);
		GL30.glVertexAttribPointer(index, size, GL30.GL_FLOAT, false, 0, 0);
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
	}



}
