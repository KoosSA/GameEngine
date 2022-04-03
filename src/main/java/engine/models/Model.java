package engine.models;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.assimp.AIColor4D;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMaterial;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIString;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.Assimp;
import org.lwjgl.opengl.GL30;

import com.koossa.filesystem.CommonFolders;
import com.koossa.filesystem.Files;
import com.koossa.logger.Log;

import engine.utils.Loader;
import engine.utils.Material;
import engine.utils.Texture;

public class Model {
	
	private List<Mesh> meshList;
	private String name;

	public Model(String name) {
		this.name = name;
		loadModelFromFile(name);
	}
	
	public List<Mesh> getMeshList() {
		return meshList;
	}
	
	public void dispose() {
		Log.info(getClass(), "Disposing model: " + name);
		meshList.forEach(mesh -> {
			GL30.glDeleteVertexArrays(mesh.getRawModel().getVaoId());
		});
	}
	
	private void loadModelFromFile(String name) {
		Log.info(this, "Loading model: " + name);
		AIScene scene = Assimp.aiImportFile(Files.getCommonFolderPath(CommonFolders.Models) + "/" + name,
				Assimp.aiProcess_Triangulate | Assimp.aiProcess_GenSmoothNormals | Assimp.aiProcess_FixInfacingNormals | Assimp.aiProcess_GenUVCoords);
		if (scene == null) {
			Log.error(this, "Failed to load model: " + name);
		}
		int numMesh = scene.mNumMeshes();
		int numMats = scene.mNumMaterials();
		meshList = new ArrayList<>();
		List<Material> mats = new ArrayList<Material>();

		for (int i = 0; i < numMats; i++) {
			AIMaterial aiMat = AIMaterial.create(scene.mMaterials().get(i));
			processMaterial(aiMat, mats);
			aiMat.free();
		}

		for (int i = 0; i < numMesh; i++) {
			AIMesh aiMesh = AIMesh.create(scene.mMeshes().get(i));
			float[] vertices = new float[aiMesh.mNumVertices() * 3];
			float[] normals = new float[aiMesh.mNumVertices() * 3];
			float[] texCoords = new float[aiMesh.mNumVertices() * 2];
			int[] indices = new int[aiMesh.mNumFaces() * 3];
			processMesh(aiMesh, vertices, normals, texCoords, indices);
			
			Material material = mats.get(aiMesh.mMaterialIndex());
			
			Mesh mesh = new Mesh(Loader.loadModelData(vertices, texCoords, normals, indices), material);
	
			meshList.add(mesh);
			aiMesh.free();
		}

		scene.free();

	}
	
	private void processMaterial(AIMaterial aiMat, List<Material> mats) {
		Material mat = new Material();
		AIColor4D colour = AIColor4D.create();
		
		AIString path = AIString.create();
		Assimp.aiGetMaterialTexture(aiMat, Assimp.aiTextureType_DIFFUSE, 0, path, (IntBuffer) null, null, null, null, null, null);
		if (path.length() > 0 && path != null) {
			String[] str = path.dataString().split("\\\\");
			Texture diffuse = Loader.getTexture(str[str.length-1]);
			mat.setDiffuse(diffuse);
		} else {
			//mat.setTintAmount(1);
			Assimp.aiGetMaterialColor(aiMat, Assimp.AI_MATKEY_COLOR_DIFFUSE, 0, 0, colour);
			mat.setColour(colour.r(), colour.g(), colour.b(), colour.a());
		}
		
		mats.add(mat);
	}

	private void processMesh(AIMesh aiMesh, float[] vertices, float[] normals, float[] texCoords, int[] indices) {
		AIVector3D.Buffer aiV = aiMesh.mVertices();
		AIVector3D.Buffer aiN = aiMesh.mNormals();
		AIVector3D.Buffer aiT = aiMesh.mTextureCoords(0);
		int counter = 0;

		while (aiV.hasRemaining()) {
			AIVector3D v = aiV.get();
			vertices[counter * 3] = v.x();
			vertices[counter * 3 + 1] = v.y();
			vertices[counter * 3 + 2] = v.z();
			counter++;
		}
		counter = 0;
		aiV.free();

		while (aiN.hasRemaining()) {
			AIVector3D v = aiN.get();
			normals[counter * 3] = v.x();
			normals[counter * 3 + 1] = v.y();
			normals[counter * 3 + 2] = v.z();
			counter++;
		}
		counter = 0;
		aiN.free();
		
		while (aiT.hasRemaining()) {
			AIVector3D v = aiT.get();
			texCoords[counter * 2] = v.x();
			texCoords[counter * 2 + 1] = v.y();
			counter++;
		}
		counter = 0;
		aiT.free();

		AIFace.Buffer aiF = aiMesh.mFaces();
		while (aiF.hasRemaining()) {
			IntBuffer f = aiF.get().mIndices();
			indices[counter * 3] = f.get(0);
			indices[counter * 3 + 1] = f.get(1);
			indices[counter * 3 + 2] = f.get(2);
			counter++;
		}
		counter = 0;
	}

}
