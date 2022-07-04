package engine.renderers;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL46;

import engine.managers.AssetManager;
import engine.models.Model;
import engine.models.ModelInstance;
import engine.shaders.StaticShader;
import engine.utils.Camera;
import engine.utils.MathUtils;

public class StaticRenderer {

	private StaticShader shader;
	private Camera cam;

	public StaticRenderer(Camera cam) {
		shader = new StaticShader();
		this.cam = cam;
		shader.start();
		shader.loadProjectionMatrix(cam.getProjectionMatrix());
		shader.stop();
	}

	public void render(Map<Model, List<ModelInstance>> list) {
		shader.start();

		shader.loadViewMatrix(cam.getViewMatrix());
		shader.loadLight(AssetManager.getLights().get(0));

		list.keySet().forEach(model -> {
			model.getMeshList().forEach(mesh -> {

				shader.loadMaterial(mesh.getMaterial());

				GL46.glBindVertexArray(mesh.getRawModel().getVaoId());

				list.get(model).forEach(instance -> {

					shader.loadTransformationMatrix(MathUtils.getTransformationMatrix(instance.getPosition(), instance.getRotation(), instance.getScale()));

					GL46.glDrawElements(GL46.GL_TRIANGLES, mesh.getRawModel().getCount(), GL46.GL_UNSIGNED_INT, 0);

				});


				GL46.glBindVertexArray(0);

			});
		});

		shader.stop();
	}

	public void dispose() {
		shader.dispose();
	}

}
