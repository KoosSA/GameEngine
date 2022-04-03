package engine.renderers;

import org.lwjgl.opengl.GL30;

import engine.shaders.TerrainShader;
import engine.terrain.TerrainGenerator;
import engine.utils.Camera;

public class TerrainRenderer {

	private TerrainShader shader;
	private Camera cam;

	public TerrainRenderer(Camera cam) {
		shader = new TerrainShader();
		this.cam = cam;
		shader.start();
		shader.loadProjectionMatrix(cam.getProjectionMatrix());
		shader.stop();
	}

	public void render() {
		shader.start();

		shader.loadViewMatrix(cam.getViewMatrix());

		TerrainGenerator.getChunkList().forEach(chunk -> {
			GL30.glBindVertexArray(chunk.getRawmodel().getVaoId());

			shader.loadMaterial(chunk.getMaterial());

			GL30.glDrawElements(GL30.GL_TRIANGLES, chunk.getRawmodel().getCount(), GL30.GL_UNSIGNED_INT, 0);

			GL30.glBindVertexArray(0);
		});

		shader.stop();
	}

	public void dispose() {
		shader.dispose();
	}
}
