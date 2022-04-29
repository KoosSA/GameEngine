package engine.renderers;

import org.lwjgl.opengl.GL46;

import engine.shaders.TerrainShader;
import engine.terrain.TerrainGenerator;
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
		shader.stop();
	}

	public void render() {
		shader.start();

		shader.loadViewMatrix(cam.getViewMatrix());

		TerrainGenerator.getToRender().forEach(chunk -> {
			GL46.glBindVertexArray(chunk.getRawmodel().getVaoId());

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
