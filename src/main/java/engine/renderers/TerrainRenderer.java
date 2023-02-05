package engine.renderers;

import org.lwjgl.opengl.GL46;

import engine.managers.AssetManager;
import engine.shaders.TerrainShader;
import engine.terrain.TerrainManager;
import engine.utils.Camera;
import engine.utils.MathUtils;

public class TerrainRenderer {

	private TerrainShader shader;
	private Camera cam;

	public TerrainRenderer(Camera cam) {
		shader = new TerrainShader();
		this.cam = cam;
		shader.start();
		shader.loadProjectionMatrix(cam.getProjectionMatrix());
		shader.loadAmbientLight(AssetManager.getAmbientLight());
		shader.stop();
	}

	public void render() {
		shader.start();

		shader.loadCamera(cam);
		shader.loadDirectionalLight(AssetManager.getDirectionalLight());

		//GL30.glDisable(GL30.GL_CULL_FACE);
		//GL30.glPolygonMode(GL30.GL_FRONT_AND_BACK, GL30.GL_LINE);
		//GL30.glLineWidth(3);

		TerrainManager.getToRender().forEach(chunk -> {
			GL46.glBindVertexArray(chunk.getRawmodel().getVaoId());
			//System.out.println(chunk.getRawmodel().getVaoId());

			shader.loadMaterial(chunk.getMaterial());
			shader.loadTransformationMatrix(MathUtils.getTransformationMatrix(chunk.getPosition()));

			GL46.glDrawElements(GL46.GL_TRIANGLES, chunk.getRawmodel().getCount(), GL46.GL_UNSIGNED_INT, 0);

			GL46.glBindVertexArray(0);
		});

		shader.stop();
	}

	public void dispose() {
		shader.dispose();
	}
}
